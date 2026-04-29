<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Atlas - Cadastrar Funcionário</title>
</head>
<body>
 
  <header>
    <a href="#">
      <img src="logo.png" alt="Atlas - Gestão de Biblioteca" />
    </a>
  </header>
 
  <main>
    <nav>
      <a href="#">início</a>
      <span>/</span>
      <span>Cadastrar funcionário</span>
    </nav>
 
    <h1>Cadastrar Funcionário</h1>
    <p>Cadastra-se para acessar.</p>
 
    <form action="#" method="post">
 
      <div>
        <label for="nome">Nome completo</label>
        <input type="text" id="nome" name="nome" placeholder="insira o nome" />
      </div>
 
      <div>
        <label for="cpf">CPF</label>
        <input type="text" id="cpf" name="cpf" placeholder="*** *** ***-**" maxlength="14" />
      </div>
 
      <div>
        <label for="nomeSocial">Nome Social</label>
        <input type="text" id="nomeSocial" name="nomeSocial" placeholder="insira seu nome social" />
      </div>
 
      <div>
        <label for="email">Email</label>
        <input type="email" id="email" name="email" placeholder="insira o e-mail" />
      </div>
 
      <div>
        <label for="telefone">Telefone</label>
        <input type="tel" id="telefone" name="telefone" placeholder="+DD (**) *****-****" />
      </div>
 
      <div>
        <label for="dataNascimento">Data de nascimento</label>
        <input type="date" id="dataNascimento" name="dataNascimento" />
      </div>
 
      <div>
        <label for="rg">RG</label>
        <input type="text" id="rg" name="rg" placeholder="insira seu número do RG" />
      </div>
 
      <fieldset>
        <legend>Sexo</legend>
        <label>
          <input type="radio" name="sexo" value="masculino" checked /> Masculino
        </label>
        <label>
          <input type="radio" name="sexo" value="feminino" /> Feminino
        </label>
        <label>
          <input type="radio" name="sexo" value="outro" /> Outro
        </label>
      </fieldset>
 
      <div>
        <label for="senha">Criar senha</label>
        <input type="password" id="senha" name="senha" placeholder="senha numérica" />
      </div>
 
      <div>
        <label for="confirmarSenha">Confirma a senha</label>
        <input type="password" id="confirmarSenha" name="confirmarSenha" placeholder="senha numérica" />
      </div>
 
      <fieldset>
        <legend>Funcionário</legend>
        <label>
          <input type="radio" name="cargo" value="atendente" checked /> Atendente
        </label>
        <label>
          <input type="radio" name="cargo" value="bibliotecario" /> Bibliotecário
        </label>
      </fieldset>
 
      <button type="submit">Finalizar cadastro</button>
 
    </form>
  </main>
 
  <footer>
    <p>© 2026 Atlas. Todos os direitos reservados.</p>
  </footer>
 
</body>
</html>