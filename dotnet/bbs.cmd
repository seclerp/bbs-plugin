@echo off
:: Batch section for Windows
if not "%OS%" == "" (
    color 0A
    echo Welcome to the fake BBS script.
    color 0E
    echo Entry point: %1
    color 0C
    echo Profile: %2
    ping -n 6 127.0.0.1 > nul
    exit /b
)

# Bash section for Unix environments
echo -e "\033[32mWelcome to the fake BBS script.\033[0m"
echo -e "\033[33mEntry point: $1\033[0m"
echo -e "\033[31mProfile: $2\033[0m"
sleep 5
