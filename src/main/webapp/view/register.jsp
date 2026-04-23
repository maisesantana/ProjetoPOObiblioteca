<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cadastrar Cliente - Atlas</title>

    <%@ include file="/partials/head.jsp" %>

</head>
<body>

    <!-- ========== HEADER / LOGO ========== -->
    <header class="atlas-header text-center py-3">
        <div class="atlas-logo">
            <span class="atlas-logo-icon">📚</span>
            <span class="atlas-logo-title">ATLAS</span>
        </div>
        <div class="atlas-logo-sub">GESTÃO DE BIBLIOTECA</div>
    </header>

    <!-- ========== CONTAINER PRINCIPAL ========== -->
    <main class="container my-4">

        <!-- Breadcrumb: Início / Cadastrar cliente -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb atlas-breadcrumb">
                <li class="breadcrumb-item"><a href="#">Início</a></li>
                <li class="breadcrumb-item active">Cadastrar cliente</li>
            </ol>
        </nav>

        <!-- Título da página -->
        <h2 class="atlas-page-title">Cadastrar cliente</h2>
        <p class="atlas-page-subtitle">Cadastra-se para acessar os livros</p>

        <!-- ========== FORMULÁRIO ========== -->
        <form action="<%=request.getContextPath()%>/registerClient" method="post" class="atlas-form-box">

            <div class="row g-3">

                <!-- Nome completo -->
                <div class="col-md-6">
                    <label class="form-label">Nome completo</label>
                    <input type="text" class="form-control" name="name" placeholder="Insira o nome" required>
                </div>

                <!-- CPF -->
                <div class="col-md-6">
                    <label class="form-label">CPF</label>
                    <input type="text" class="form-control" name="cpf" placeholder="***.***.***-**" required>
                </div>

                <!-- Nome Social -->
                <div class="col-md-6">
                    <label class="form-label">Nome Social</label>
                    <input type="text" class="form-control" name="socialName" placeholder="Insira seu nome social">
                </div>

                <!-- Email -->
                <div class="col-md-6">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" placeholder="Insira o e-mail">
                </div>

                <!-- Telefone -->
                <div class="col-md-6">
                    <label class="form-label">Telefone</label>
                    <input type="text" class="form-control" name="phone" placeholder="+DD(**)****-****">
                </div>

                <!-- Data de nascimento -->
                <div class="col-md-6">
                    <label class="form-label">Data de nascimento</label>
                    <input type="date" class="form-control" name="birthDate">
                </div>

                <!-- RG -->
                <div class="col-md-6">
                    <label class="form-label">RG</label>
                    <input type="text" class="form-control" name="rg" placeholder="Insira seu número do RG">
                </div>

                <!-- Sexo - radio buttons -->
                <div class="col-md-6">
                    <label class="form-label">Sexo</label>
                    <div class="atlas-radio-group">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="gender" value="MASCULINO" id="masculino">
                            <label class="form-check-label" for="masculino">Masculino</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="gender" value="FEMININO" id="feminino">
                            <label class="form-check-label" for="feminino">Feminino</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="gender" value="OUTRO" id="outro">
                            <label class="form-check-label" for="outro">Outro</label>
                        </div>
                    </div>
                </div>

                <!-- Situação do curso - radio buttons -->
                <div class="col-md-6">
                    <label class="form-label">Situação do curso</label>
                    <div class="atlas-radio-group">
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="isStudying" value="true" id="cursando">
                            <label class="form-check-label" for="cursando">Cursando</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="isStudying" value="false" id="naoCursando">
                            <label class="form-check-label" for="naoCursando">Não cursando</label>
                        </div>
                    </div>
                </div>

                <!-- CEP -->
                <div class="col-md-6">
                    <label class="form-label">CEP</label>
                    <input type="text" class="form-control" name="zipCode" placeholder="**.***.***">
                </div>

                <!-- Ano de saída do curso -->
                <div class="col-md-6">
                    <label class="form-label">Ano de saída do curso</label>
                    <input type="number" class="form-control" name="graduationYear" placeholder="****">
                </div>

                <!-- Id do curso -->
                <div class="col-md-6">
                    <label class="form-label">Id do curso</label>
                    <select class="form-select" name="courseId">
                        <option value="">Selecione...</option>
                        <%-- Adicione as opções vindas do banco aqui --%>
                    </select>
                </div>

                <!-- Ano de entrada do curso -->
                <div class="col-md-6">
                    <label class="form-label">Ano de entrada do curso</label>
                    <input type="number" class="form-control" name="entryYear" placeholder="****">
                </div>

                <!-- Endereço -->
                <div class="col-md-6">
                    <label class="form-label">Endereço</label>
                    <input type="text" class="form-control" name="address" placeholder="Rua, nº e Bairro">
                </div>

                <!-- Senha -->
                <div class="col-md-6">
                    <label class="form-label">Senha</label>
                    <input type="password" class="form-control" name="password" required>
                </div>

            </div>

            <!-- Botão finalizar -->
            <div class="mt-4">
                <button type="submit" class="atlas-btn-submit w-100">
                    Finalizar cadastro
                </button>
            </div>

        </form>

    </main>

    <!-- FOOTER -->
    <footer class="atlas-footer text-center py-3">
        <small>© 2026 Atlas. Todos os direitos reservados.</small>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>