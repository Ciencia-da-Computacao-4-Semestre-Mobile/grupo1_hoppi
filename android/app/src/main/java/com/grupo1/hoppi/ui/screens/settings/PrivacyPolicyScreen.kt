package com.grupo1.hoppi.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    navController: NavController
) {

    val p0 = "Política de Privacidade – Hoppi\n\nÚltima atualização: [21/11/2025]\n\n" +
            "A sua privacidade é importante para nós. Esta Política de Privacidade descreve " +
            "como o Hoppi coleta, utiliza e protege as informações pessoais dos usuários. " +
            "Ao utilizar o aplicativo ou acessar nossos serviços, você concorda com os termos descritos abaixo."

    val p1 = "1. Informações que coletamos\n\n" +
            "O Hoppi coleta apenas os dados necessários para criação e uso da sua conta. " +
            "Esses dados são fornecidos diretamente por você no momento do cadastro. São eles:\n\n" +
            "• Nome completo\n" +
            "• Data de nascimento\n" +
            "• E-mail\n" +
            "• Instituição de ensino\n\n" +
            "Não coletamos nenhuma outra informação além das listadas acima."

    val p2 = "2. Informações que NÃO coletamos\n\n" +
            "O Hoppi não solicita, acessa ou armazena:\n\n" +
            "• Fotos, vídeos ou arquivos da galeria\n" +
            "• Localização\n" +
            "• Contatos\n" +
            "• Microfone ou câmera\n" +
            "• Histórico de navegação\n" +
            "• Dados sensíveis não relacionados ao uso do app\n\n" +
            "Nos comprometemos a coletar apenas o mínimo necessário para o funcionamento da plataforma."

    val p3 = "3. Como utilizamos suas informações\n\n" +
            "Os dados coletados são utilizados exclusivamente para:\n\n" +
            "• Criar e manter sua conta no Hoppi\n" +
            "• Permitir interação entre usuários dentro do aplicativo\n" +
            "• Melhorar a experiência de uso da plataforma\n" +
            "• Garantir autenticidade de perfis e segurança para a comunidade\n" +
            "• Realizar comunicações relacionadas ao uso do app (como avisos e notificações importantes)\n\n" +
            "Não utilizamos suas informações para fins de marketing externo."

    val p4 = "4. Compartilhamento de informações\n\n" +
            "O Hoppi não vende, não compartilha e não repassa seus dados pessoais a terceiros " +
            "para fins comerciais.\n\n" +
            "O compartilhamento só poderá ocorrer em situações específicas:\n\n" +
            "• Quando exigido por lei, ordem judicial ou autoridade competente\n" +
            "• Para garantir a segurança do sistema e prevenir fraudes\n\n" +
            "Mesmo nesses casos, seguiremos todas as exigências legais de proteção de dados."

    val p5 = "5. Armazenamento e segurança\n\n" +
            "Seus dados são armazenados de forma segura e protegidos por práticas adequadas de " +
            "criptografia, autenticação e controle de acesso.\n\n" +
            "Apesar disso, nenhum sistema digital é 100% protegido. Em caso de incidente de segurança, " +
            "você será informado conforme exigido pela legislação aplicável."

    val p6 = "6. Direitos do usuário\n\n" +
            "Você tem direito a:\n\n" +
            "• Acessar suas informações pessoais\n" +
            "• Corrigir dados incompletos ou desatualizados\n" +
            "• Solicitar a exclusão da sua conta e dos dados armazenados\n" +
            "• Revogar o consentimento a qualquer momento\n" +
            "• Obter informações sobre o tratamento dos seus dados\n\n" +
            "Para exercer seus direitos, entre em contato pelos nossos canais de suporte."

    val p7 = "7. Exclusão de dados\n\n" +
            "Ao solicitar a exclusão da conta, todos os seus dados pessoais serão removidos " +
            "de forma definitiva, exceto quando houver obrigação legal de mantê-los por tempo determinado."

    val p8 = "8. Links externos\n\n" +
            "O Hoppi pode conter links para sites externos. Não nos responsabilizamos pelas práticas " +
            "de privacidade desses serviços. Recomendamos que você leia as políticas de privacidade " +
            "de cada site visitado."

    val p9 = "9. Alterações nesta Política\n\n" +
            "Esta Política de Privacidade pode ser atualizada periodicamente para refletir melhorias, " +
            "mudanças legais ou novos recursos. A data da última atualização será sempre informada " +
            "no início do documento.\n\n" +
            "Quando alterações importantes forem realizadas, você será notificado."

    val p10 = "10. Contato\n\n" +
            "Em caso de dúvidas, solicitações ou questões relacionadas à privacidade, entre em contato pelo nosso e-mail oficial:\n\n" +
            "📩 hoppihophophop0104@gmail.com"

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = TopAppBarDefaults.windowInsets,
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Política de Privacidade",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HoppiOrange,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingInterno)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            listOf(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10).forEach { paragraph ->
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = paragraph,
                        textAlign = TextAlign.Justify,
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}


