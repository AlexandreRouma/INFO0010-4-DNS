$BUILD_DIR = 'build'

# Compile
mkdir -ErrorAction ignore $BUILD_DIR
Get-ChildItem -Path src/ -Filter *.java -Recurse -ErrorAction SilentlyContinue -Force | %{$_.FullName} | Out-File -Encoding ascii -FilePath $BUILD_DIR/sources.txt
javac -d $BUILD_DIR @$BUILD_DIR/sources.txt