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
            pkgs.docker
            pkgs.docker-compose
          ];

          shellHook = ''
            # Check if Docker daemon is running
            if ! docker info > /dev/null 2>&1; then
              echo "[ERROR] Docker daemon is not running. Please start Docker and try again."
              exit 1
            fi

            export DB_HOST=localhost
            export DB_PORT=5432
            export DB_USER=postgres
            export DB_PASSWORD=password
            export NODE_ENV=development
            export JAVA_HOME=${pkgs.openjdk17}
            
            echo "[SUCCESS] Added environment variables. OneDollarBid is ready!"
            echo "[DOCKER] Starting Dockerized PostgreSQL..."
            docker-compose up -d

            echo "Development environment for OneDollarBid is ready!"
            echo "Environment variables:"
            echo "  DB_HOST: $DB_HOST"
            echo "  DB_PORT: $DB_PORT"
            echo "  DB_USER: $DB_USER"
            echo "  DB_PASSWORD: $DB_PASSWORD"
            echo "  NODE_ENV: $NODE_ENV"
            echo "  JAVA_HOME: $JAVA_HOME"
          '';

          shellExitHook = ''
            echo "Stopping Dockerized PostgreSQL..."
            docker-compose down
          '';
        };
      }
    );
}
