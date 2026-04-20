# EPITO - Logiciel de gestion de pharmacie

Application de bureau Java/JavaFX permettant de gérer les utilisateurs, les rôles et les modules métier d'une pharmacie (laboratoire, prélèvements).

Dépôt : https://gitlab.com/axellr/erp-axel.git

---

## Technologies utilisées

- **Langage :** Java 21
- **Interface graphique :** JavaFX 21 (FXML)
- **Build :** Gradle (Kotlin DSL)
- **Base de données :** MariaDB
- **Accès données :** JDBI3
- **Sécurité :** BCrypt (hachage des mots de passe)
- **Gestion de version :** Git & GitLab

---

## Prérequis

- Java 21 (JDK)
- Gradle 8+
- XAMPP (ou tout serveur MariaDB) avec une base `erp` accessible sur le port `3307`

---

## Installation

### 1. Cloner le dépôt

```bash
git clone https://gitlab.com/axellr/erp-axel.git
cd projet-erp-evan
```

### 2. Configurer la base de données

Créer une base de données `erp` sur votre serveur MariaDB.
Les paramètres de connexion se trouvent dans `UserDAO.java` et `RoleDAO.java` :

```
URL      : jdbc:mariadb://127.0.0.1:3307/erp
Utilisateur : root
Mot de passe : (vide par défaut)
```

Modifier ces valeurs si nécessaire avant de lancer l'application.

### 3. Lancer l'application

```bash
./gradlew run
```

---

## Structure du projet

```
app/src/main/java/cc/lery/
├── controller/         # Contrôleurs JavaFX (connexion, dashboard, ajout utilisateur)
├── dao/                # Accès base de données (UserDAO, RoleDAO)
├── model/              # Modèles métier (User, Role)
├── service/            # Couche service (UserService, RoleService)
├── session/            # Gestion de la session utilisateur
├── AppScene.java       # Gestionnaire de navigation entre les vues
└── Launcher.java       # Point d'entrée de l'application
```

---

## Packaging

Pour générer un installateur natif (`.exe`, `.deb` ou `.dmg` selon l'OS) :

```bash
./gradlew jpackage
```

L'exécutable généré s'appellera **EPITO**.

---

## Fonctionnalités

- Authentification avec mot de passe haché (BCrypt)
- Gestion des utilisateurs (création, liste)
- Gestion des rôles
- Session utilisateur
- Vues : connexion, tableau de bord, laboratoire, prélèvements

---

## Auteur

Axel Leroy — Lery CC
