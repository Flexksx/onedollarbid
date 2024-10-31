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
            pkgs.postgresql
            pkgs.docker
          ];
          shellHook = ''
            export DB_HOST=localhost
            export DB_PORT=5432
            export DB_USER=postgres
            export DB_PASSWORD=password
            export NODE_ENV=development
            export JAVA_HOME=${pkgs.openjdk11}
            echo "Development environment for OneDollarBid is ready!"
          '';
        };
      }
    );
}
