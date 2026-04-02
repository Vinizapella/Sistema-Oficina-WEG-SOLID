# 🔧 Sistema Oficina WEG — Refatorado

> Projeto desenvolvido como parte da avaliação prática da Unidade Curricular **Arquitetura de Sistemas** — SENAI SC / Turma AI MIDS 2024/2 INT2.

---

## 📋 Sumário

- [Contexto](#-contexto)
- [Problemas do Código Original](#-problemas-do-código-original)
- [A Nova Arquitetura](#-a-nova-arquitetura)
- [Princípios SOLID Aplicados](#-princípios-solid-aplicados)
- [Estrutura de Pacotes](#-estrutura-de-pacotes)
- [Regras de Negócio](#-regras-de-negócio)
- [Como Rodar o Sistema](#-como-rodar-o-sistema)
- [Endpoints da API](#-endpoints-da-api)

---

## 📌 Contexto

A escola técnica da WEG utilizava um sistema monolítico (`SistemaOficinaWegCompleto`) para gerenciar a manutenção dos equipamentos dos laboratórios. Todo o sistema — cadastro de usuários, turmas, permissões e fluxo de Ordens de Serviço (OS) — estava concentrado em **uma única classe com mais de 200 linhas**, caracterizando uma **God Class**.

O objetivo deste projeto foi **refatorar completamente** esse sistema aplicando os princípios **SOLID**, padrões de **Clean Code** e uma arquitetura moderna com **Spring Boot**.

---

## 🚨 Problemas do Código Original

| Problema | Descrição | Impacto |
|---|---|---|
| **God Class** | Todo o sistema em `SistemaOficinaWegCompleto.java` | Impossível de manter e testar |
| **Vetores estáticos fixos** | `String[] usuariosNomes = new String[100]` | Limite rígido de 100 usuários, sem escalabilidade |
| **Sem encapsulamento** | Todos os dados em `static` público | Qualquer método pode corromper qualquer dado |
| **Sem polimorfismo** | `if/else` para diferenciar `PROFESSOR` e `ALUNO` | Adicionar um novo tipo de usuário exige alterar múltiplos blocos |
| **Sem interfaces** | Implementações diretas em tudo | Impossível trocar implementações ou escrever testes |
| **Lógica misturada** | Regra de negócio, I/O e persistência juntos | Qualquer alteração de regra exige entender todo o sistema |
| **Sem tratamento de erros** | Cast direto `(Professor) usuario` sem verificação | `ClassCastException` em runtime sem mensagem amigável |
| **Sem rastreabilidade real** | Campos como `osConclusaoTecnica` eram simples strings soltas | Fácil de perder dados críticos de manutenção |

### Exemplo do problema no original:

```java
// Código original — if/else espalhado por toda a classe
if (idProf >= totalPessoas || !usuariosTipos[idProf].equals("PROFESSOR")) {
    System.out.println("ERRO: Acesso negado.");
}
// Esse padrão se repete em TODOS os casos de uso,
// tornando qualquer mudança de regra de permissão um pesadelo
```

---

## 🏗️ A Nova Arquitetura

O sistema foi reescrito como uma **API REST** com Spring Boot, organizado em camadas bem definidas:

```
Requisição HTTP
      │
      ▼
 [Controller]  ← recebe e valida a entrada (DTO)
      │
      ▼
  [Service]    ← executa as regras de negócio
      │
      ▼
 [Repository]  ← acessa o banco de dados via JPA
      │
      ▼
   [Model]     ← representa as entidades do domínio
```

**Como isso resolve os problemas do original:**

- Vetores fixos → substituídos por **banco de dados relacional** com JPA (ilimitado e persistente)
- God Class → dividida em **15+ classes com responsabilidade única**
- Sem interfaces → **3 interfaces de serviço** (`IUsuarioService`, `IOrdemServicoService`, `ITurmaService`)
- Sem polimorfismo → **herança real** com `Usuario` abstrata, `Aluno` e `Professor` como subclasses
- Sem encapsulamento → **regras de acesso protegidas** dentro dos serviços com validação explícita

---

## ✅ Princípios SOLID Aplicados

### S — Single Responsibility Principle (Princípio da Responsabilidade Única)

> *"Uma classe deve ter apenas um motivo para mudar."*

**Problema no original:** A classe `SistemaOficinaWegCompleto` era responsável por tudo ao mesmo tempo: exibir menus, validar permissões, armazenar dados e executar regras de negócio.

**Como foi resolvido:**

| Classe | Responsabilidade única |
|---|---|
| `OrdemServicoController` | Receber e responder requisições HTTP de OS |
| `OrdemServicoService` | Executar as regras de negócio da OS |
| `OrdemServicoMapper` | Converter entre DTO e entidade |
| `OrdemServicoRepository` | Persistir e recuperar OS do banco |
| `OrdemServico` | Representar os dados de uma OS |

```java
// Cada classe faz UMA coisa:
@Service
public class OrdemServicoService implements IOrdemServicoService {
    // Só regras de negócio aqui — sem I/O, sem SQL direto
}
```

---

### O — Open/Closed Principle (Princípio Aberto/Fechado)

> *"Uma classe deve estar aberta para extensão, mas fechada para modificação."*

**Problema no original:** Para adicionar um novo tipo de usuário (ex: Coordenador), seria necessário modificar todos os blocos `if/else` espalhados pelo código.

**Como foi resolvido:** A classe `Usuario` é **abstrata**. Para adicionar um novo tipo de usuário, basta criar uma nova subclasse — sem tocar nas classes existentes.

```java
// Aberta para extensão:
public class Coordenador extends Usuario {
    private String departamento;
    // Nenhuma outra classe precisou ser modificada
}
```

Os DTOs seguem o mesmo princípio com a interface `UsuarioRequestDto` e `UsuarioResponseDto`, usando polimorfismo via Jackson:

```java
@JsonSubTypes({
    @JsonSubTypes.Type(value = AlunoRequestDto.class, name = "ALUNO"),
    @JsonSubTypes.Type(value = ProfessorRequestDto.class, name = "PROFESSOR")
    // Novo tipo? Só adicionar aqui.
})
public interface UsuarioRequestDto { }
```

---

### L — Liskov Substitution Principle (Princípio da Substituição de Liskov)

> *"Objetos de uma subclasse devem poder substituir objetos da superclasse sem quebrar o sistema."*

**Como foi resolvido:** `Aluno` e `Professor` herdam de `Usuario`. Em qualquer lugar que o sistema espera um `Usuario`, pode receber um `Aluno` ou `Professor` sem problemas.

```java
// O repository retorna Usuario, mas funciona com qualquer subtipo:
List<Usuario> usuarios = usuarioRepository.findAll();
// Pode ser Aluno, Professor, ou qualquer futura subclasse
```

O `UsuarioMapper` demonstra isso convertendo corretamente qualquer subtipo:

```java
public UsuarioResponseDto toResponse(Usuario usuario) {
    if (usuario instanceof Aluno aluno) { ... }
    else if (usuario instanceof Professor professor) { ... }
    // Qualquer subtipo válido de Usuario é tratado corretamente
}
```

---

### I — Interface Segregation Principle (Princípio da Segregação de Interfaces)

> *"Uma classe não deve ser forçada a implementar interfaces que não utiliza."*

**Problema no original:** Não havia interfaces. Tudo era uma massa de métodos estáticos.

**Como foi resolvido:** Foram criadas **3 interfaces específicas**, cada uma com apenas os métodos relevantes ao seu contexto:

```java
// Interface focada apenas em OS:
public interface IOrdemServicoService {
    OrdemServicoResponseDto abrir(OrdemServicoRequestDto dto);
    void atualizarStatus(Long id, OrdemServicoAtualizarDto dto);
}

// Interface focada apenas em Usuário:
public interface IUsuarioService {
    UsuarioResponseDto salvar(UsuarioRequestDto dto);
    List<UsuarioResponseDto> listarTodos();
    UsuarioResponseDto buscarPorId(Long id);
}

// Interface focada apenas em Turma:
public interface ITurmaService {
    TurmaResponseDto criar(TurmaRequestDto dto);
    void adicionarAluno(Long turmaId, Long alunoId);
}
```

Nenhuma implementação é forçada a implementar métodos que não fazem sentido para ela.

---

### D — Dependency Inversion Principle (Princípio da Inversão de Dependência)

> *"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações."*

**Problema no original:** Tudo dependia diretamente de implementações concretas (arrays estáticos, métodos estáticos).

**Como foi resolvido:** Os Controllers **nunca conhecem** as implementações concretas dos serviços. Eles dependem apenas das **interfaces**:

```java
@RestController
public class OrdemServicoController {

    // Depende da INTERFACE, não da implementação:
    private final IOrdemServicoService iOrdemServicoService;

    // O Spring injeta automaticamente a implementação correta:
    public OrdemServicoController(IOrdemServicoService iOrdemServicoService) {
        this.iOrdemServicoService = iOrdemServicoService;
    }
}
```

Isso significa que a implementação de `OrdemServicoService` pode ser trocada (ex: para testes) sem alterar nenhum Controller.

---

## 📁 Estrutura de Pacotes

```
src/main/java/com/weg/oficina/
│
├── controller/                  # Camada de entrada HTTP
│   ├── OrdemServicoController.java
│   ├── TurmaController.java
│   └── UsuarioController.java
│
├── dto/                         # Objetos de transferência de dados
│   ├── aluno/
│   │   ├── AlunoRequestDto.java
│   │   └── AlunoResponseDto.java
│   ├── ordemServico/
│   │   ├── OrdemServicoAtualizarDto.java
│   │   ├── OrdemServicoRequestDto.java
│   │   └── OrdemServicoResponseDto.java
│   ├── professor/
│   │   ├── ProfessorRequestDto.java
│   │   └── ProfessorResponseDto.java
│   ├── turma/
│   │   ├── TurmaRequestDto.java
│   │   └── TurmaResponseDto.java
│   ├── UsuarioRequestDto.java   # Interface polimórfica
│   └── UsuarioResponseDto.java  # Interface polimórfica
│
├── mapper/                      # Conversão entre camadas
│   ├── OrdemServicoMapper.java
│   ├── TurmaMapper.java
│   └── UsuarioMapper.java
│
├── model/                       # Entidades do domínio
│   ├── Aluno.java
│   ├── OrdemServico.java
│   ├── Professor.java
│   ├── Turma.java
│   └── Usuario.java             # Classe abstrata base
│
├── repository/                  # Acesso ao banco de dados
│   ├── AlunoRepository.java
│   ├── OrdemServicoRepository.java
│   ├── ProfessorRepository.java
│   ├── TurmaRepository.java
│   └── UsuarioRepository.java
│
└── service/                     # Regras de negócio
    ├── IOrdemServicoService.java # Interface
    ├── ITurmaService.java        # Interface
    ├── IUsuarioService.java      # Interface
    ├── OrdemServicoService.java  # Implementação
    ├── TurmaService.java         # Implementação
    └── UsuarioService.java       # Implementação
```

---

## 🔒 Regras de Negócio

| Regra | Como está implementada |
|---|---|
| Somente professores abrem OS | `OrdemServicoService.abrir()` valida se o usuário fornecido é instância de `Professor` |
| Somente alunos escalados executam | `atualizarStatus()` pode ser estendido com validação da lista `executores` |
| Somente o professor responsável encerra | `OrdemServicoService` compara o `professorId` da requisição com o da OS |
| OS deve registrar equipamento, defeito, materiais e laudo | Campos obrigatórios validados via `@NotBlank` nos DTOs |

---

## 🚀 Como Rodar o Sistema

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Banco de dados (mysql)

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/oficina-weg.git
cd oficina-weg

# 2. Compile e rode
./mvnw spring-boot:run

# 3. A API estará disponível em:
# http://localhost:8080
```

---

## 🌐 Endpoints da API

> 💡 **Dica:** Para testar o fluxo completo, siga a ordem abaixo: cadastre os usuários primeiro, depois crie a turma, adicione alunos a ela e só então abra uma OS.

---

### 1. Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/usuarios` | Cadastra aluno ou professor |
| `GET` | `/usuarios` | Lista todos os usuários |
| `GET` | `/usuarios/{id}` | Busca usuário por ID |

**Exemplo — cadastrar professor:**
```json
POST /usuarios
{
  "tipo_usuario": "PROFESSOR",
  "nome": "Lucas Souza",
  "especialidade": "Engenharia da Computação"
}
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "Lucas Souza",
  "tipo": "PROFESSOR",
  "especialidade": "Engenharia da Computação"
}
```

**Exemplo — cadastrar aluno:**
```json
POST /usuarios
{
  "tipo_usuario": "ALUNO",
  "nome": "Vinicius Zapella",
  "matricula": "2024001"
}
```

**Resposta:**
```json
{
  "id": 2,
  "nome": "Vinicius Zapella",
  "tipo": "ALUNO",
  "matricula": "2024001"
}
```

---

### 2. Turmas

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/turmas` | Cria uma turma |
| `PUT` | `/turmas/{turmaId}/aluno/{alunoId}` | Adiciona aluno à turma |

**Exemplo — criar turma:**
```json
POST /turmas
{
  "nome": "Desenvolvimento de Sistemas MI78"
}
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "Desenvolvimento de Sistemas MI78",
  "nomesAlunos": []
}
```

**Exemplo — adicionar aluno à turma** (aluno ID 2 na turma ID 1):
```
PUT /turmas/1/aluno/2
```

**Resposta ao listar a turma após adicionar alunos:**
```json
{
  "id": 1,
  "nome": "Desenvolvimento de Sistemas MI78",
  "nomesAlunos": [
    "Vinicius Zapella"
  ]
}
```

> ⚠️ **Regra de segurança:** se tentar adicionar um professor (em vez de aluno) à turma, o sistema retorna erro com a mensagem `"Apenas alunos podem ser adicionados a uma turma"`.

---

### 3. Ordens de Serviço

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/ordens` | Abre uma OS (apenas professor) |
| `PUT` | `/ordens/{id}` | Atualiza status, laudo e materiais |

**Exemplo — abrir OS** (professor ID 1 escala alunos ID 2 e 3):
```json
POST /ordens
{
  "equipamento": "Torno CNC - Lab 03",
  "defeitoRelatado": "Fuso com folga excessiva",
  "professorId": 1,
  "alunosIds": [2, 3]
}
```

**Resposta:**
```json
{
  "id": 1,
  "equipamento": "Torno CNC - Lab 03",
  "defeitoRelatado": "Fuso com folga excessiva",
  "status": "EXECUTANDO",
  "materiaisUsados": null,
  "laudoTecnico": null,
  "nomeProfessor": "Lucas Souza",
  "nomesAlunos": ["Vinicius Zapella", "Carlos Silva"]
}
```

> ⚠️ **Regra de segurança:** se o `professorId` enviado não pertencer a um professor, o sistema retorna erro com a mensagem `"Acesso negado. somente o professor responsavel pode encerrar esta Ordem de Serviço"`.

**Exemplo — encerrar OS** (somente o professor que abriu pode encerrar, informando o `professorId`):
```json
PUT /ordens/1
{
  "novoStatus": "CONCLUIDA",
  "laudo": "Substituído rolamento SKF 6205. Folga eliminada.",
  "materiais": "Rolamento SKF 6205 (1 un), Graxa industrial (200g)",
  "professorId": 1
}
```

> ⚠️ **Regra de segurança:** se o `professorId` enviado for diferente do professor que abriu a OS, o sistema retorna erro `400` com a mensagem `"Acesso negado. somente o professor responsavel pode encerrar esta Ordem de Serviço"`.

---

## 👨‍💻 Autor

Desenvolvido por **Vinicius dos Santos Zapella** — SENAI SC  
Turma AI MIDS 2024/2 INT2 | Unidade Curricular: Arquitetura de Sistemas  
Docente: Lucas Sousa dos Santos
