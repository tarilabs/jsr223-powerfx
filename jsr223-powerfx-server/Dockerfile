FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build-env
WORKDIR /app

# Copy everything
COPY . ./
# Restore as distinct layers
RUN dotnet restore
# Build and publish a release
RUN dotnet publish -c Release -o out

# Build runtime image
FROM mcr.microsoft.com/dotnet/aspnet:6.0
WORKDIR /app
COPY --from=build-env /app/out .

# TODO For development purposes we don't bind https certificate (yet)
ENV ASPNETCORE_ENVIRONMENT=Development

# see also: ASPNETCORE_URLS, ASPNETCORE_HTTPS_PORT
# https://github.com/dotnet/dotnet-docker/blob/main/samples/run-aspnetcore-https-development.md
# https://github.com/dotnet/dotnet-docker/blob/main/samples/run-in-sdk-container.md

ENTRYPOINT ["dotnet", "jsr223-powerfx-server.App.dll"]