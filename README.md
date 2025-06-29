# 📚 Lista Smart App

&#x20;&#x20;

## 🌟 Objetivo

O **Lista Smart App** é um sistema de segmentação de emails que permite:

- Cadastro de usuários com ID incremental
- Consulta por ID e e-mail
- Listagem geral de usuários
- Atualização e exclusão de usuários
- Filtragem por tags
- Salvar dados em arquivo local
- Exportar lista para CSV
- Utilização tanto via **interface gráfica (Swing)** quanto via **console**

---

## 🛠 Tecnologias Utilizadas

- **Java 24**
- **Swing** (Interface Gráfica)
- **IntelliJ IDEA**

---

## 📁 Estrutura do Projeto

```
org.listasmart
 ├── Main.java
 ├── User.java
 ├── interfaces
 │     ├── GUI.java
 │     └── Console.java
 └── services
       ├── UserService.java
       └── FileHandler.java
```

---

## 🚀 Como Executar

### ✅ **1. Via IDE**

1. Abra o projeto no **IntelliJ IDEA** ou **VS Code**
2. Navegue até `Main.java`
3. Clique em **Run**

---

### 🔧 **2. Via Terminal**

1. Compile os arquivos:

```bash
javac -d bin src/org/listasmart/*.java src/org/listasmart/interfaces/*.java src/org/listasmart/services/*.java
```

2. Execute o projeto:

```bash
java -cp bin org.listasmart.Main
```

---

## ⚠ Problemas Conhecidos

- IDs foram alterados de UUID para inteiros auto incrementais para melhor usabilidade no console e GUI.
- Em alguns ambientes, pode ser necessário configurar manualmente o `launch.json` no VS Code para rodar a Main.

---

## ✨ Melhorias Futuras

- Persistência em banco de dados (ex: SQLite)
- Interface gráfica em JavaFX
- Implementação de testes unitários com JUnit
- Deploy via JAR executável com ícone e splash screen

---

## 👨‍💼 Autores

** Arthur Silva **

- [GitHub](https://github.com/Artthorius)
- [LinkedIn](https://www.linkedin.com/in/arthurrsilva/)
- Email: [arthusilva1648@gmail.com](mailto\:arthursilva1648@gmail.com)

** Pietro Lorenzo ** 

- [GitHub](https://github.com/pietrolorenzo)
- Email: [pietro_lorenzo@outlook.com](mailto\:pietro_lorenzo@outlook.com)

** Fernando Bartilotti ** 

- [GitHub](https://github.com/Bartilottti)
- [LinkedIn](https://www.linkedin.com/in/fernandobartilotti/) 
- Email: [fmbartilotti@gmail.com](mailto\:fmbartilotti@gmail.com)

** Gustavo Galvão **

- [GitHub](https://github.com/gustavojgalvao)
- [LinkedIn](https://www.linkedin.com/in/gustavo-galvãoo/)
- Email: [gustavojezler@gmail.com](mailto\:gustavojezler@gmail.com)
---

## 📄 Licença

Uso acadêmico

---

> *Este projeto foi desenvolvido como exercício prático de programação Java, focando no uso de orientação a objetos, estruturas de dados e desenvolvimento de interface gráfica com Swing.*

