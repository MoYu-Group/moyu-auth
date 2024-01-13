# Please open file with UTF-16

# 设置环境变量
[Environment]::SetEnvironmentVariable("DATASOURCE_URL", "数据库url", "User")
[Environment]::SetEnvironmentVariable("DB_USERNAME", "数据库账户", "User")
[Environment]::SetEnvironmentVariable("DB_PASSWORD", "数据库密码", "User")

# 获取环境变量
$datasourceUrl = [Environment]::GetEnvironmentVariable("DATASOURCE_URL", "User")
$dbUsername = [Environment]::GetEnvironmentVariable("DB_USERNAME", "User")
$dbPassword = [Environment]::GetEnvironmentVariable("DB_PASSWORD", "User")

# 打印设置结果
Write-Host "环境变量设置完成:"
Write-Host "DATASOURCE_URL: $datasourceUrl"
Write-Host "DB_USERNAME: $dbUsername"
Write-Host "DB_PASSWORD: $dbPassword"