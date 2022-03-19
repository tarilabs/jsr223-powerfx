export ASPNETCORE_ENVIRONMENT=Development
dotnet run

docker build -t jsr223-powerfx-server -f Dockerfile .
docker images

docker rm jsr223-powerfx-server

docker create -p 5000:80 --name jsr223-powerfx-server jsr223-powerfx-server
docker start jsr223-powerfx-server
docker stop jsr223-powerfx-server
