# Trabalho Prático AEDs3
Trabalho feito com base nas templates oferecidas no repositório [AEDsIII](https://github.com/kutova/AEDsIII), visando a criação de um sistema com a implementação de um CRUD das entidades *Série*, *Episódio* e *Ator*, com controle preciso do armazenamento em arquivos.
- [Parte 1 do trabalho](https://github.com/L0L0VIS/TP-Aeds3)

# Participantes
- [Mateus Resende Ottoni](https://github.com/Mateus-Resende-Ottoni)
- [Mateus Ribeiro Fernandes](https://github.com/L0L0VIS)


# Resumo de funcionalidades

## O que o programa faz?
O programa permite a criação das entidades *Série*, *Episódio* e *Ator* (as quais herdam da interface *Registro*) , que contêm os seguintes atributos:

Série:
- ID
- Nome
- Sinopse
- Ano de lançamento
- Streaming

Episódio:
- ID
- ID Serie
- Nome
- Número
- Temporada
- Data de lançamento
- Duração

Ator:
- ID
- Nome

As entidades criadas são armazenadas em forma de arquivo (um para cada tipo), além de cada arquivo ser acompanhado por arquivos de índice, os quais facilitam a busca, usando a estrutura provida em *HashExtensivel*. Além disso, há a implementação da conexão entre as entidades, feita pela estrutura *ArvoreBMais*, as quais existem em duas formas:
- Relação 1:N entre Série e Episódio: *Episódio* contém em si uma chave estrangeira de *Série*. Para essa relação, há uma árvore B+ que conecta uma série a seus episódios.
- Relação N:N entre Série e Ator: Duas árvores B+, ambas utilizando do *ParIDID*, uma organizada segundo o ID da *Série*, e outra pelo ID do *Ator*.

## Nosso trabalho
Nosso trabalho foi de converter e adaptar as template mencionadas anteriormente, considerando as diferentes necessidades das entidades com as quais trabalhamos, como a entidade *Episódio* tendo referência à classe *Série*. Tivemos dificuldade especialmente no que diz respeito a tratar as estruturas *ArvoreBMais* e *HashExtensivel*, a considerar o quão detalhado os seus códigos são, havendo dificuldades especialmente no que diz respeito ao método de deletar.

## Resumo via checklist
- [X] *"As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente?"*
- [X] *"O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades?"*
- [X] *"É possível consultar quais são os atores de uma série?"*
- [X] *"É posssível consultar quais são as séries de um ator?"*
- [X] *"A remoção de séries remove os seus vínculos de atores?"*
- [X] *"A inclusão de um ator em uma série em um episódio se limita aos atores existentes?"*
- [X] *"A remoção de um ator checa se há alguma série vinculado a ele?"*
- [X] *"A inclusão da série em um episódio se limita às séries existentes?"*
- [X] *"O trabalho está funcionando corretamente?"*
- [X] *"O trabalho está completo?"*
- [X] *"O trabalho é original e não a cópia de um trabalho de outro grupo?"*


# Funcionalidades das classes
## *Arquivos*

### Arquivo
Formato base do arquivo para salvar informações das entidades, contendo os métodos de CRUD, e cabeçalho que registra último ID e último espaço com registro deletado.

### ArquivoAtor
Estrutura e métodos de arquivo adaptados à entidade *Ator*, incluindo métodos para associar a séries.
### ArquivoEpisodio
Estrutura e métodos de arquivo adaptados à entidade *Episódio*, incluindo métodos para recuperar e deletar grupo de episódios com base no ID de série.
### ArquivoSerie
Estrutura e métodos de arquivo adaptados à entidade *Série*, incluindo métodos para associar a atores.

## *Entidades*

### Registro
Interface base para composição das entidades, contendo os métodos que são necessários de forma universal.

### ChaveComposta
Chave usada para organização da entidade *Episodio* na estrutura *ArvoreBMais*.
### ParIDEndereco
Chave usada como indice direto na estrutura *HashExtensivel*.
### ParIDID
Chave usada como conexão das entidades *Ator* e *Série* na estrutura *ArvoreBMais*.
### ParNomeID
Chave usada como indice indireto pela entidade *Serie* na estrutura *HashExtensivel*.

### Ator
Estrutura básica da entidade *Ator*, contendo os atributos e seus respectivos gets e sets.
### Episodio
Estrutura básica da entidade *Episodio*, contendo os atributos e seus respectivos gets e sets.
### Serie
Estrutura básica da entidade *Serie*, contendo os atributos e seus respectivos gets e sets.

## *Estruturas*

### RegistroArvoreBMais
Interface com os necessário métodos à estrutura *ArvoreBMais*.
### ArvoreBMais
Estrutura *ArvoreBMais*, com métodos de inserção de novo elemento (que possui os necessários meios para aumentar o tamanho atual da estrutura quando o número de elementos ultrapassar o máximo atual), leitura e remoção.

### RegistroHashExtensivel
Interface com os necessário métodos à estrutura *HashExtensivel*.
### Hash Extensivel
Estrutura *HashExtensivel*, com métodos de leitura, inserção, atualização e remoção. Divide-se em Diretório e Cesto, em acordo com aquilo a ser salvo.

## *Menus*

### MenuAtor
Acesso e chamada via interface às operações CRUD da entidade *Ator*.
### MenuEpisodio
Acesso e chamada via interface às operações CRUD da entidade *Episodio*.
### MenuSeries
Acesso e chamada via interface às operações CRUD da entidade *Serie*.

## Principal
Acesso via interface aos menus das respectivas classes.
