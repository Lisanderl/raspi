echo 0
git pull origin
echo 1
mvn spring-boot:run -Dspring.profiles.active=prod
