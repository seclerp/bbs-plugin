@echo off
:: Batch section for Windows
if not "%OS%" == "" (
    echo Welcome to the fake BBS script.
    echo Entry point: %1
    echo Profile: %2
    ping -n 6 127.0.0.1 > nul
    exit /b
)

# Bash section for Unix environments
echo "Welcome to the fake BBS script."
echo "Entry point: $1"
echo "Profile: $2"
sleep 5
