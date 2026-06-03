# Atlas — Sistema de Gestão de Biblioteca

Sistema web de gestão de biblioteca desenvolvido em Java com Jakarta Servlet, JSP, MySQL e Jetty.

---

## Como rodar o projeto

### Pré-requisitos
- [Docker Desktop](https://www.docker.com/products/docker-desktop) instalado
- [Maven](https://maven.apache.org/) instalado

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/maisesantana/ProjetoPOObiblioteca
cd ProjetoPOObiblioteca

# 2. Gere o WAR
mvn clean package

# 3. Suba o sistema
docker-compose up
```

Acesse: **http://localhost:8080/view/index.jsp**

---

## Usuários do sistema

### Administrador
| Campo | Valor |
|-------|-------|
| CPF | `00000000000` |
| Senha | `123456` |

### Atendentes
| Nome | CPF | Senha |
|------|-----|-------|
| João Pedro Martins | `44444444444` | `1234` |
| Fernanda Rocha | `55555555555` | `5678` |

### Bibliotecários
| Nome | CPF | Senha |
|------|-----|-------|
| Ricardo Mendes | `66666666666` | `4321` |
| Juliana Ferreira | `77777777777` | `8765` |

---

## Perfis e permissões

### Administrador
- Cadastrar, editar e remover funcionários
- Visualizar lista de funcionários

### Bibliotecário
- Cadastrar, remover livros e exemplares
- Gerenciar autores e categorias
- Buscar livros no acervo

### Atendente
- Gerenciar clientes (cadastrar, editar, remover)
- Registrar empréstimos
- Registrar devoluções
- Registrar renovações
- Consultar empréstimos ativos
- Buscar livros

---

##  Clientes de teste

| Nome | CPF | Status |
|------|-----|--------|
| Ana Beatriz Souza | `11111111111` | Ativo |
| Carlos Henrique Lima | `22222222222` | Ativo |
| Mariana Alves Costa | `33333333333` | Ativo |
| Pedro Henrique Silva | `88888888888` | Suspenso |
| Lucia Fernanda Reis | `99999999999` | Ativo |

---

## Stack tecnológica

| Tecnologia | Versão |
|------------|--------|
| Java | 17 |
| Jakarta Servlet | 6.0 |
| Jetty | 11.0.15 |
| MySQL | 8.0 |
| Bootstrap | 5.3.3 |
| Maven | 3.x |

---

## Estrutura do projeto

```
src/main/java/br/com/atlas/
  controller/    → Controllers shell (terminal)
  dao/           → Acesso ao banco de dados
  model/         → Modelos de domínio
  service/       → Regras de negócio
  util/          → Utilitários (ConnectionDb)
  view/          → Views shell (terminal)
  webcontroller/ → Controllers web (servlets)

src/main/webapp/
  assets/css/    → Folhas de estilo
  assets/images/ → Imagens
  view/          → Páginas JSP
  WEB-INF/       → Configuração web.xml

sql/
  atlas.sql      → Script DDL + dados iniciais
```

---

## Equipe

Projeto desenvolvido para a disciplina de Programação Orientada a Objetos.

| Integrante |
|------------|
| Maise Santana dos Santos |
| MIQUEIAS NOGUEIRA SANTOS |
| ÉRICA ELLEN ARAÚJO DE BARROS |
| ANA RODE RODRIGUES DOS SANTOS |
| RAYANE GABRIELLE CASTRO CARDOSO |