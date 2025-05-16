@echo off
echo ================================
echo   🚀 Iniciando microservicio Inventario
echo ================================

REM Cambia al directorio del proyecto
cd /d %~dp0

REM Verifica si el servicio de MySQL está corriendo
echo ✅ Verificando si MySQL está activo...
sc query "MySQL80" | findstr /I "RUNNING" >nul
if %errorlevel% neq 0 (
    echo ❌ MySQL no está corriendo. Por favor, inicia el servicio manualmente desde XAMPP o Workbench.
    pause
    exit /b
)

REM Borrar carpeta target (limpiar compilados)
echo 🔄 Limpiando proyecto...
mvn clean

REM Ejecutar el proyecto
echo 🚀 Ejecutando Spring Boot...
mvn spring-boot:run

pause
