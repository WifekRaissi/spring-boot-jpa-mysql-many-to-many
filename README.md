# spring-boot-jpa-mysql-many-to-many

Dans les deux tutoriaux précédants on a étudié le mapping dans les relation One to Many et One to One :

https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-many

https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-one

On finit dans le présent tutorial par la relation Many to Many entre la table Salarie et Projet. Un salarié peut intégrer plusieurs projets et un projet peut être affecté à plusieurs salariés.
L'architecture du projet est la suivante:

![alt text](https://github.com/WifekRaissi/spring-boot-jpa-mysql-many-to-many/blob/master/src/main/resources/architecture.PNG)       

