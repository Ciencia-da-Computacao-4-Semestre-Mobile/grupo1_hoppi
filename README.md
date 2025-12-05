# 🐇 Hoppi - Se tem estudante, tem Hoppi

Este projeto é uma rede social voltada para estudantes, com o objetivo de promover interação, troca de ideias e criação de comunidades. Os usuários podem publicar mensagens de texto, seguir colegas e participar de comunidades de interesse. Como primeira versão, o aplicativo estará disponível apenas para modelos Android.

### Tecnologias utilizadas (em resumo)

  - **Front-end**:
    - Kotlin
    - Jetpack Compose
  - **Back-end**:
    - Node.js
    - Nest.js
    - Supabase/Postgres
   

### Documentação
https://www.notion.so/Documenta-o-2b82405855c880be84e3cd849c9ee592

## 🚀 Como rodar o projeto localmente

Para clonar e executar o projeto localmente, siga os passos abaixo:

```bash

git clone <https://github.com/Ciencia-da-Computacao-4-Semestre-Mobile/grupo1_hoppi.git>

```
+ Não se esqueça de criar e configurar o arquivo ".env". Como modelo para definição dessas variáveis de ambiente, utilize o arquivo ".env.example".

## Padrão de desenvolvimento

Esse projeto será dividido em branches de desenvolvimento separadas por camadas (backend e frontend) e uma geral. Ao começar uma nova atividade, acesse a branch relacionada a camada que desenvolverá e, ao finalizar sua contribuição, crie um commit detalhado, que descreva o código desenvolvido.

## Regras de nomenclatura de commits

Nomeie seu novo commit seguindo os padrões abaixo.
Seu commit deve conter 2 partes:

1. Tipo de mudança/funcionalidade:
   - **build**: mudanças na estrutura/arquitetura do projeto (criação e exclusão de pastas e/ou exclusão de arquivos);
   - **docs**: apenas mudanças na documentação;
   - **feat**: nova funcionalidade (mais utilizado);
   - **fix**: correção de bugs;
   - **test**: adicionar ou corrigir testes;
   - **perf**: mudança de código para melhorar sua performance;
   - **refactor**: mudança de código que não adiciona uma funcionalidade e também não corrige um bug;
   - **style**: mudanças no código que não afetam seu significado (espaço em branco, formatação, ponto e vírgula, etc).

3. O que o código faz:
   - Descreva de forma resumida e com palavras-chaves a funcionalidade do seu código.

**Exemplo de commit:**
feat: user registration functionality

---
