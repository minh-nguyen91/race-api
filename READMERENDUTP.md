TP — API REST : Gestion d'inscriptions à une course

Ce projet est une API REST développée en Java et Spring Boot pour gérer des coureurs, des courses, et leurs inscriptions. Les données sont stockées sur une base PostgreSQL.

Comment lancer le projet

1. Démarrer la base de données (nécessite Docker) :
   ```bash
   docker compose up -d
2.	Démarrer l'application :
Bash
mvn spring-boot:run

Toutes les fonctionnalités demandées ont été développées, incluant la gestion des erreurs HTTP (404, 409, 400) et les règles métier (vérification d'email, places limitées, pas de double inscription).

Coureurs
•	GET /runners : Lister les coureurs
•	GET /runners/{id} : Récupérer un coureur
•	POST /runners : Créer un coureur
•	PUT /runners/{id} : Modifier un coureur
•	DELETE /runners/{id} : Supprimer un coureur
•	GET /runners/{runnerId}/races : Voir les courses d'un coureur

Courses
•	GET /races : Lister les courses
•	GET /races/{id} : Récupérer une course
•	POST /races : Créer une course
•	PUT /races/{id} : Modifier une course
•	GET /races/{raceId}/participants/count : Compter les inscrits à une course

Inscriptions
•	POST /races/{raceId}/registrations : Inscrire un coureur à une course
•	GET /races/{raceId}/registrations : Voir la liste des participants d'une course

Le code a été découpé de manière standard pour Spring Boot, en séparant bien les responsabilités :

•	1. Les Entités (Entities) : Ce sont de simples classes Java (Runner, Race, Registration) annotées avec @Entity. Elles représentent exactement la structure des tables dans la base de données PostgreSQL. (C'est là qu'on a utilisé l'astuce @JdbcTypeCode pour corriger le problème de type avec la base de données).
•	2. Les Repositories : Ce sont des interfaces qui héritent de JpaRepository. Grâce à ça, Spring Boot génère automatiquement toutes les requêtes SQL de base (sauvegarder, trouver par ID, supprimer). On a aussi ajouté des méthodes sur-mesure, comme existsByRunnerIdAndRaceId(...) pour vérifier les doublons.
•	3. Les Services (Le "Cerveau") : C'est ici qu'on a codé les règles métier demandées par l'énoncé. Les services utilisent les repositories pour lire/écrire en base de données, mais font des vérifications avant :
o	Est-ce que l'email contient un @ ?
o	Est-ce que la course a atteint son maxParticipants ?
o	Est-ce que le coureur est déjà inscrit ?
•	4. Les Contrôleurs (Controllers) : C'est la porte d'entrée de l'API. Ils interceptent les requêtes HTTP de Postman (GET, POST, etc.) grâce aux annotations comme @GetMapping ou @PostMapping. Ils appellent ensuite le bon Service et renvoient le résultat en JSON à l'utilisateur.

La gestion des erreurs (Codes HTTP)
Pour respecter les codes demandés par le TP (400, 404, 409) :
•	On a utilisé ResponseStatusException dans les Services.
•	Si on cherche un coureur qui n'existe pas, le service "jette" une exception HttpStatus.NOT_FOUND (404).
•	Si un coureur tente de s'inscrire à une course pleine, le service jette une exception HttpStatus.CONFLICT (409).
•	Pour forcer les créations à renvoyer un 201 Created au lieu du 200 par défaut, on a simplement ajouté l'annotation @ResponseStatus(HttpStatus.CREATED) au-dessus des méthodes POST et PUT dans les contrôleurs.
