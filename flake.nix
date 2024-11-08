{
  description = "OneDollarBid Development Environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs =
    {
      self,
      nixpkgs,
      flake-utils,
      ...
    }:
    flake-utils.lib.eachDefaultSystem (
      system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = [
            pkgs.openjdk17
            pkgs.maven
            pkgs.nodejs
            pkgs.yarn
            pkgs.docker
            pkgs.docker-compose
          ];

          shellHook = ''
            if ! docker info > /dev/null 2>&1; then
              echo -e "\e[31m[ERROR]\e[0m Docker daemon is not running. Please start Docker and try again."
              exit 1
            fi

            export DB_HOST=localhost
            export DB_PORT=5432
            export DB_USER=postgres
            export DB_PASSWORD=password
            export NODE_ENV=development
            export JAVA_HOME=${pkgs.openjdk17}

            echo -e "\e[32m[SUCCESS]\e[0m Added environment variables. OneDollarBid is ready!"
            echo -e "\e[32m[DOCKER]\e[0m Starting Dockerized PostgreSQL..."
            docker-compose up -d

            echo -e "\e[32mDevelopment environment for OneDollarBid is ready!\e[0m"
            echo "Environment variables:"
            echo "  DB_HOST: $DB_HOST"
            echo "  DB_PORT: $DB_PORT"
            echo "  DB_USER: $DB_USER"
            echo "  DB_PASSWORD: $DB_PASSWORD"
            echo "  NODE_ENV: $NODE_ENV"
            echo "  JAVA_HOME: $JAVA_HOME"
            echo "  SPRING_PORT: $SPRING_PORT"
            echo "Cleaning maven cache..."
            cd onedollarbid
            mvn clean install
            cd ..
            echo -e "\e[32m[SUCCESS]\e[0m Maven cache cleaned!"
          '';

          shellExitHook = ''
            docker-compose down
            echo -e "\e[31mStopping Dockerized PostgreSQL...\e[0m"
          '';
        };
      }
    );
}
