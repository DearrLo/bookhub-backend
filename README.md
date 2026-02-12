# 📚 BookHub - Système de Gestion de Bibliothèque
BookHub est une application fullstack conçue pour digitaliser la gestion d'une bibliothèque. Elle permet de gérer un catalogue d'ouvrages, d'automatiser les prêts et de gérer les files d'attente via un système de réservation

# 🚀 BookHub API - Backend Service

Ce dépôt contient le service Backend de l'application **BookHub**. Il s'agit d'une API REST robuste construite avec Spring Boot, gérant la logique métier, la persistance des données et la sécurité via JWT.


## 🛠 Stack Technique
* **Langage :** Java 17
* **Framework :** Spring Boot 4
* **Sécurité :** Spring Security & JWT (Stateless)
* **Persistance :** Spring Data JPA / Hibernate
* **Base de données :** H2 (Mode persistant pour le développement)
* **Outils :** Lombok, Gradle, Javadoc


## ⚙️ Installation et Lancement

1. **Cloner le projet**
 
``` bash
git clone https://github.com/ton-pseudo/bookhub.git
cd bookhub
```

2. **Configuration de la base de donnée:**
Les paramètres de base de données et la clé secrète JWT se trouvent dans `src/main/resources/application.properties`.
   Décommentez la connexion Dcoker à la base de donnnée et commentez la connexion locale.
   Lancer:
   ``` bash
   docker compose up -d
   ```

3. **Lancer l'application :**

```bash
./gradlew bootRun
```

4. **Run le Backend (Spring Boot)**
Assurez-vous d'avoir un JDK 17 installé.
Run `BookhubApplication.java`

L'API sera accessible sur : http://localhost:8080
   

🧪 Tests et Qualité
Tests Unitaires : JUnit 5 & Mockito (vérification de la logique des Services).

Lancement des tests : 
```bash
./gradlew test
```

📖 Documentations :

Documentation Javadoc : Générez la doc complète de l'application avec 
```./gradlew javadoc```
(disponible ensuite dans build/docs/javadoc/)

Swagger UI : Une fois le serveur lancé, accédez à la documentation interactive des endpoints 
🔗 `http://localhost:8080/swagger-ui/index.html`



URL : http://localhost:8080/h2-console

JDBC URL : jdbc:h2:mem:bookhub (ou selon config)
