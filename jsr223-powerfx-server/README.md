export ASPNETCORE_ENVIRONMENT=Development
dotnet run

docker build -t jsr223-powerfx-server-image -f Dockerfile .
docker images

docker create -p 5000:80 --name jsr223-powerfx-server jsr223-powerfx-server-image
docker start jsr223-powerfx-server
docker stop jsr223-powerfx-server
