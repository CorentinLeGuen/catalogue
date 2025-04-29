# 📚 Catalogue

> Application permettant à une librairie en ligne de gérer son catalogue de livres.
> L'application doit permettre:
> - **Consulter** les livres
> - **Ajouter**, **Modifier** et **Supprimer** des livres
> - **Rechercher** des livres par titre ou auteur

## Installation

🐳 Conteneurs docker détachés (option -d)
```
docker-compose up --build -d
```
## Stack technique

- Java & 🍃 Spring
- Postgresql
- Jacoco

## Tests
```shell
mvn clean verify
```

-> Le rapport de couverture de tests est disponible [ici](/target/site/jacoco/index.html).

## TODO

- [ ] Retravailler les logs (couverture à ~92% actuellement)
- [ ] SonarQube: Changer *@MockBean* des tests (déprécié depuis la version 3.4.0)
- [ ] Tester hedgecases
- [ ] Ajouter des loggers + Prometheus + Grafana
- [ ] Ajouter des jetons JWT pour une authentification stateless