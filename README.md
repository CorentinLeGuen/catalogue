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

⚠️ Le build va échouer si [la couverture de tests n'attend pas 80%](https://github.com/CorentinLeGuen/catalogue/blob/main/pom.xml#L167).

## Stack technique

- Java & 🍃 Spring
- Postgresql
- Jacoco

[![swagger docs](https://img.shields.io/badge/Swagger%20UI-darkseagreen?style=for-the-badge&logo=swagger)](http://localhost:8080/swagger-ui/index.html)

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