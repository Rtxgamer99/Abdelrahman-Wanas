@echo off
echo Running data migration...
mvn compile exec:java -Dexec.mainClass="src.DataMigration"
echo Migration completed!
pause 