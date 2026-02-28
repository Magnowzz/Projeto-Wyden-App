# Aplicativo Android para Lanchonete

Aplicativo Android desenvolvido em Kotlin com Firebase para gerenciamento de pedidos e pesquisa de satisfação de uma lanchonete.

## 📋 Plano de Trabalho Detalhado

### Ação 1 - Levantar Requisitos junto aos Colaboradores da Lanchonete
**Prazo:** 1 semana  
**Recursos Necessários:**
- Reuniões com equipe da lanchonete (gerente, garçons, cozinheiros)
- Formulário de levantamento de requisitos
- Documentação das necessidades

**Atividades:**
- Entrevistas com funcionários para identificar necessidades
- Mapeamento do fluxo de trabalho atual
- Definição de funcionalidades essenciais:
  - Cadastro de pedidos
  - Controle de status (Pendente, Preparando, Pronto, Entregue)
  - Cálculo automático de totais
  - Pesquisa de satisfação dos clientes
  - Relatórios de avaliações

**Entregáveis:**
- Documento de requisitos funcionais
- Protótipos de telas
- Lista de prioridades

---

### Ação 2 - Modelar e Desenvolver o Aplicativo utilizando Tecnologia Android
**Prazo:** 4-6 semanas  
**Recursos Necessários:**
- Ambiente de desenvolvimento Android Studio
- Conta Firebase (gratuita)
- Dispositivo Android ou emulador para testes
- Conhecimento em Kotlin e Firebase

**Tecnologias Utilizadas:**
- **Linguagem:** Kotlin
- **Plataforma:** Android (API 24+)
- **Backend:** Firebase Firestore
- **Arquitetura:** MVVM (Model-View-ViewModel)
- **UI:** Material Design Components

**Estrutura do Aplicativo:**

#### Funcionalidades Implementadas:

1. **Tela Principal (MainActivity)**
   - Lista de pedidos em tempo real
   - Visualização de status de cada pedido
   - Botão para criar novo pedido
   - Botão para pesquisa de satisfação
   - Atualização de status dos pedidos

2. **Criar Pedido (CriarPedidoActivity)**
   - Cadastro de cliente
   - Adição de múltiplos itens ao pedido
   - Cálculo automático de subtotais e total
   - Campo de observações
   - Salvamento no Firebase

3. **Pesquisa de Satisfação (PesquisaSatisfacaoActivity)**
   - Avaliação por estrelas (1-5)
   - Campo de comentários
   - Estatísticas de avaliações (média e total)
   - Vinculação com pedido específico

#### Modelos de Dados:

- **Pedido:** Cliente, itens, total, status, data, observações
- **ItemPedido:** Nome, quantidade, preço, subtotal
- **Avaliacao:** Pedido ID, nota, comentário, data, cliente

**Entregáveis:**
- Código-fonte do aplicativo
- Aplicativo compilado (APK)
- Documentação técnica

---

### Ação 3 - Testar o Aplicativo com Dados Reais e Ajustar Funcionalidades
**Prazo:** 2 semanas  
**Recursos Necessários:**
- Dispositivos Android para testes
- Dados reais de pedidos da lanchonete
- Ambiente de testes no Firebase
- Equipe de teste (funcionários da lanchonete)

**Atividades:**
- Testes unitários das funcionalidades principais
- Testes de integração com Firebase
- Testes de usabilidade com funcionários
- Testes com dados reais de pedidos
- Identificação e correção de bugs
- Ajustes de interface baseados em feedback
- Testes de performance e otimização

**Checklist de Testes:**
- [ ] Criação de pedidos com múltiplos itens
- [ ] Cálculo correto de totais
- [ ] Atualização de status
- [ ] Salvamento e recuperação de dados do Firebase
- [ ] Pesquisa de satisfação funcionando
- [ ] Estatísticas de avaliações corretas
- [ ] Interface responsiva e intuitiva
- [ ] Tratamento de erros de conexão

**Entregáveis:**
- Relatório de testes
- Lista de bugs corrigidos
- Versão ajustada do aplicativo

---

### Ação 4 - Realizar Treinamento Prático para os Funcionários
**Prazo:** 1 semana  
**Recursos Necessários:**
- Material de treinamento (apostila, vídeos)
- Dispositivos Android para prática
- Ambiente de demonstração
- Instrutor/desenvolvedor disponível

**Atividades:**
- Sessão de apresentação do aplicativo
- Demonstração prática de cada funcionalidade
- Treinamento hands-on com funcionários
- Resolução de dúvidas
- Criação de material de referência rápida

**Conteúdo do Treinamento:**
1. Instalação do aplicativo
2. Como criar um novo pedido
3. Como adicionar itens ao pedido
4. Como atualizar status do pedido
5. Como realizar pesquisa de satisfação
6. Como visualizar estatísticas
7. Solução de problemas comuns

**Entregáveis:**
- Material de treinamento
- Vídeo tutorial (opcional)
- Guia rápido de referência
- Certificação de treinamento dos funcionários

---

### Ação 5 - Aplicar Pesquisa de Satisfação para Mensurar os Resultados
**Prazo:** 2 semanas (coleta) + 1 semana (análise)  
**Recursos Necessários:**
- Aplicativo em produção
- Ferramentas de análise de dados
- Tempo dos clientes para avaliação

**Atividades:**
- Coleta de avaliações dos clientes
- Análise dos dados coletados
- Cálculo de métricas:
  - Média de satisfação
  - Total de avaliações
  - Análise de comentários
  - Tendências de satisfação
- Geração de relatório de resultados
- Apresentação dos resultados para gestão

**Métricas a Mensurar:**
- Número total de avaliações recebidas
- Média de notas (1-5 estrelas)
- Distribuição de notas
- Análise qualitativa dos comentários
- Taxa de resposta (avaliações/pedidos)
- Comparação antes/depois (se aplicável)

**Entregáveis:**
- Relatório de pesquisa de satisfação
- Dashboard com métricas
- Recomendações de melhorias
- Apresentação dos resultados

---

## 🚀 Como Configurar e Executar

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 8 ou superior
- Conta Google (para Firebase)
- Dispositivo Android ou Emulador

### Configuração do Firebase

1. Acesse o [Console do Firebase](https://console.firebase.google.com/)
2. Crie um novo projeto ou use um existente
3. Adicione um app Android ao projeto
4. Baixe o arquivo `google-services.json`
5. Substitua o arquivo `app/google-services.json` do projeto pelo arquivo baixado
6. Configure as regras do Firestore:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /pedidos/{document=**} {
      allow read, write: if true; // Em produção, adicione autenticação
    }
    match /avaliacoes/{document=**} {
      allow read, write: if true; // Em produção, adicione autenticação
    }
  }
}
```

### Instalação

1. Clone ou baixe o projeto
2. Abra o projeto no Android Studio
3. Sincronize o Gradle (Sync Project)
4. Execute o aplicativo em um dispositivo ou emulador

### Build do APK

```bash
./gradlew assembleDebug
```

O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📱 Funcionalidades do Aplicativo

### Gerenciamento de Pedidos
- ✅ Criar novos pedidos
- ✅ Adicionar múltiplos itens
- ✅ Cálculo automático de totais
- ✅ Atualizar status do pedido
- ✅ Visualizar histórico de pedidos

### Pesquisa de Satisfação
- ✅ Avaliação por estrelas (1-5)
- ✅ Comentários dos clientes
- ✅ Estatísticas de avaliações
- ✅ Média de notas
- ✅ Total de avaliações

### Integração Firebase
- ✅ Armazenamento em tempo real
- ✅ Sincronização automática
- ✅ Persistência offline

---

## 📊 Cronograma Resumido

| Ação | Prazo | Status |
|------|-------|--------|
| Ação 1 - Levantar Requisitos | 1 semana | ✅ Concluído |
| Ação 2 - Desenvolver Aplicativo | 4-6 semanas | ✅ Concluído |
| Ação 3 - Testar e Ajustar | 2 semanas | ⏳ Pendente |
| Ação 4 - Treinamento | 1 semana | ⏳ Pendente |
| Ação 5 - Pesquisa de Satisfação | 3 semanas | ⏳ Pendente |

**Prazo Total Estimado:** 11-13 semanas

---

## 🛠️ Estrutura do Projeto

```
projeto-android/
├── app/
│   ├── src/main/
│   │   ├── java/com/lanchonete/app/
│   │   │   ├── MainActivity.kt
│   │   │   ├── CriarPedidoActivity.kt
│   │   │   ├── PesquisaSatisfacaoActivity.kt
│   │   │   ├── adapter/
│   │   │   │   ├── PedidoAdapter.kt
│   │   │   │   └── ItemPedidoAdapter.kt
│   │   │   ├── model/
│   │   │   │   ├── Pedido.kt
│   │   │   │   └── Avaliacao.kt
│   │   │   └── repository/
│   │   │       ├── PedidoRepository.kt
│   │   │       └── AvaliacaoRepository.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   └── values/
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── google-services.json
├── build.gradle
├── settings.gradle
└── README.md
```

---

## 📝 Notas Importantes

- O arquivo `google-services.json` fornecido é um template. Substitua pelo arquivo real do seu projeto Firebase.
- As regras do Firestore estão configuradas para permitir leitura/escrita sem autenticação. Em produção, implemente autenticação adequada.
- O aplicativo requer conexão com internet para funcionar com Firebase.
- Para uso offline, configure a persistência do Firestore no código.

---

## 👥 Contribuição

Este é um projeto acadêmico/demonstrativo. Para melhorias ou correções, sinta-se à vontade para contribuir.

---

## 📄 Licença

Este projeto é fornecido como está, para fins educacionais e demonstrativos.

---

**Desenvolvido com ❤️ usando Kotlin e Firebase**



