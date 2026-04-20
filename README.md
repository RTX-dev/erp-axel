# EPITO — Logiciel de gestion de pharmacie

Application de bureau Java/JavaFX pour gérer les utilisateurs, les rôles, les prélèvements et le laboratoire d'une pharmacie.

Dépôt : https://gitlab.com/axellr/erp-axel.git

---

## Technologies

| Composant | Version |
|---|---|
| Java (JDK) | 21 |
| JavaFX | 21.0.2 |
| Gradle (Kotlin DSL) | 8.12.1 |
| Base de données | MariaDB (via XAMPP) |
| Accès données | JDBI3 + JDBC direct |
| Sécurité | BCrypt (jbcrypt 0.4) |

---

## Prérequis

- **JDK 21** — [télécharger](https://adoptium.net/)
- **XAMPP** (ou tout serveur MariaDB) avec MariaDB démarré sur le port `3307`

> Sur Mac, XAMPP utilise par défaut le port **3307** (et non 3306). Vérifiez dans le panneau de contrôle XAMPP que MariaDB est bien démarré.

---

## Installation

### 1. Cloner le dépôt

```bash
git clone https://gitlab.com/axellr/erp-axel.git
cd erp-axel
```

### 2. Créer la base de données

Connectez-vous à MariaDB (via phpMyAdmin ou le terminal) puis exécutez le script suivant :

```sql
-- Créer la base de données
CREATE DATABASE IF NOT EXISTS erp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE erp;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id_user   INT          NOT NULL AUTO_INCREMENT,
    lastname  VARCHAR(100) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    mail      VARCHAR(150) NOT NULL UNIQUE,
    phone     VARCHAR(20),
    password  VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_user)
);

-- Table des rôles
CREATE TABLE IF NOT EXISTS role (
    id_role       BIGINT       NOT NULL AUTO_INCREMENT,
    name_function VARCHAR(100) NOT NULL,
    description   VARCHAR(255),
    PRIMARY KEY (id_role)
);

-- Table de liaison utilisateur ↔ rôle
CREATE TABLE IF NOT EXISTS Assign (
    id_user INT    NOT NULL,
    id_role BIGINT NOT NULL,
    PRIMARY KEY (id_user, id_role),
    FOREIGN KEY (id_user) REFERENCES users(id_user)  ON DELETE CASCADE,
    FOREIGN KEY (id_role) REFERENCES role(id_role)   ON DELETE CASCADE
);

-- Table des prélèvements
CREATE TABLE IF NOT EXISTS sample (
    id_sample      BIGINT       NOT NULL AUTO_INCREMENT,
    name           VARCHAR(150) NOT NULL,
    description    TEXT,
    date_of_create DATE         NOT NULL,
    reception_date DATE         NOT NULL,
    name_patient   VARCHAR(150) NOT NULL,
    PRIMARY KEY (id_sample)
);

-- Rôles par défaut (OBLIGATOIRE — les IDs 1 et 2 sont utilisés en dur par le Launcher)
INSERT IGNORE INTO role (id_role, name_function, description) VALUES
    (1, 'ADMIN',   'Administrateur — accès complet'),
    (2, 'MEDECIN', 'Médecin — accès aux prélèvements et au laboratoire');
```

> **Important :** Les deux lignes `INSERT` dans la table `role` sont obligatoires. Le `Launcher.java` assigne le rôle ID `1` (ADMIN) et ID `2` (MEDECIN) lors de la création des comptes par défaut.

### 3. Vérifier la configuration de connexion

Les paramètres de connexion sont définis dans les trois fichiers DAO :

| Fichier | Chemin |
|---|---|
| `UserDAO.java` | `app/src/main/java/cc/lery/dao/UserDAO.java` |
| `RoleDAO.java` | `app/src/main/java/cc/lery/dao/RoleDAO.java` |
| `SampleDAO.java` | `app/src/main/java/cc/lery/dao/SampleDAO.java` |

Valeurs par défaut (XAMPP Mac) :

```
URL      : jdbc:mariadb://127.0.0.1:3307/erp
User     : root
Password : (vide)
```

Si votre MariaDB tourne sur un autre port ou avec un mot de passe, modifiez les constantes `URL`, `USER` et `PASSWORD` dans chacun de ces trois fichiers.

### 4. Lancer l'application

```bash
./gradlew run
```

Au premier lancement, le `Launcher` crée automatiquement deux comptes de test s'ils n'existent pas encore :

| Email | Mot de passe | Rôle |
|---|---|---|
| `admin@erp.com` | `admin123` | ADMIN |
| `doc@erp.com` | `doc123` | MEDECIN |

---

## Structure du projet

```
erp-axel/
├── app/
│   └── src/main/
│       ├── java/cc/lery/
│       │   ├── controller/        # Contrôleurs JavaFX (login, dashboard, ajout)
│       │   ├── dao/               # Accès BDD (UserDAO, RoleDAO, SampleDAO)
│       │   ├── model/             # Modèles métier (User, Role, Sample)
│       │   ├── service/           # Couche service (UserService, RoleService, SampleService)
│       │   ├── session/           # Session utilisateur connecté (SessionManager)
│       │   ├── AppScene.java      # Navigation entre les vues FXML
│       │   └── Launcher.java      # Point d'entrée — init DB + lancement JavaFX
│       └── resources/
│           └── view/              # Fichiers FXML (interfaces graphiques)
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew / gradlew.bat
```

---

## Packaging — générer un installateur natif

```bash
./gradlew jpackage
```

Génère un installateur natif selon l'OS :

| OS | Format | Nom |
|---|---|---|
| Windows | `.exe` | Pharmacie-Installer.exe |
| Linux | `.deb` | Pharmacie-Installer.deb |
| macOS | `.dmg` | EPITO.dmg |

> Sur macOS, l'application n'est pas signée. À la première ouverture, faire **clic droit → Ouvrir** pour contourner Gatekeeper.

---

## Fonctionnalités

- Authentification sécurisée (mots de passe hachés BCrypt)
- Gestion des utilisateurs (création, liste)
- Gestion des rôles (ADMIN, MEDECIN)
- Gestion des prélèvements (CRUD)
- Vue laboratoire
- Session utilisateur avec contrôle d'accès par rôle

---

## Auteur

Axel Leroy — Lery CC
