# Declare an associative array
declare -A env_vars

# Populate the associative array with environment variables
env_vars=(
  [DB_HOST]="localhost"
  [DB_PORT]="5432"
  [DB_USER]="postgres"
  [DB_PASSWORD]="password"
  [NODE_ENV]="development"
  [JAVA_HOME]="/nix/store/27csmcaag2l3k9pki2y74il2bczkv4v7-openjdk-17.0.12+7"
  [SPRING_PORT]="8080"
)

echo "Checking if Docker is running..."
if ! docker info > /dev/null 2>&1; then
  echo "Docker is not running. Please start Docker and try again."
  exit 1
fi
echo "\e[32m[SUCCESS]\e[0m Docker is running."

echo "Composing the Docker image..."
if ! docker-compose up -d; then
  echo "\e[31m[ERROR]\e[0m Failed to build the Docker image."
  exit 1
fi
echo "\e[32m[SUCCESS]\e[0m Docker image built successfully."

echo "Exporting environment variables..."
# Loop through the associative array to export the variables
for key in "${!env_vars[@]}"; do
  export "$key=${env_vars[$key]}"
  echo "\e[32m[SUCCESS]\e[0m $key set to ${env_vars[$key]}."
done
