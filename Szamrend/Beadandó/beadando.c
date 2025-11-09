#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define FILENAME "locsoloverseny.txt"
#define MAX_NAME 100
#define MAX_VERS 1000

typedef struct{
	char name[MAX_NAME];
	char vers[MAX_VERS];
	int eggs;
} Bunny;

void registerBunny() {
	FILE *file = fopen(FILENAME, "a");
	if(!file){
		perror("Hiba a fájl megnyitásakor");
		return;
	}
	
	Bunny b;
	printf("Nyuszi neve: ");
	scanf(" %[^\n]", b.name);
	printf("Locsoló vers: ");
	scanf(" %[^\n]", b.vers);	
	b.eggs = 0;

 	fprintf(file, "%s|%s|%d\n", b.name, b.vers, b.eggs);
    	fclose(file);
    	printf("Sikeres regisztráció!\n");
}

void listBunnies() {
    FILE *file = fopen(FILENAME, "r");
    if (!file) {
        printf("Nincsenek regisztrált nyuszik.\n");
        return;
    }
    
    char line[1500];
    while(fgets(line,sizeof(line),file)){
       char *name = strtok(line, "|");
       char *verse = strtok(NULL, "|");
       char *eggsStr = strtok(NULL, "|");
	if (name && verse && eggsStr) {
            printf("Név: %s, Versike: %s, Tojások: %s", name, verse, eggsStr);
        }
    }
    
    fclose(file);
}

void modifyBunny() {
    FILE *file = fopen(FILENAME, "r");
    if (!file) {
        printf("Nincsenek regisztrált nyuszik.\n");
        return;
    }

    Bunny bunnies[100];
    int count = 0;
    char line[1500];
    while(fgets(line,sizeof(line),file)){
	strncpy(bunnies[count].name,strtok(line, "|"),MAX_NAME);
	strncpy(bunnies[count].vers,strtok(NULL, "|"),MAX_VERS);
       bunnies[count].eggs = atoi(strtok(NULL, "|"));
	count+=1;
    }
    fclose(file);
    
    char name[MAX_NAME];
    printf("Melyik nyuszi adatait szeretnéd módosítani? ");
    scanf(" %[^\n]", name);
    
    for (int i = 0; i < count; i++) {
        if (strcmp(bunnies[i].name, name) == 0) {
	    int choice;
            printf("\n1. Név módosítása\n");
            printf("2. Locsoló vers módosítása\n");
            printf("3. Tojások számának módosítása\n");
            printf("Válassz egy opciót: ");
            scanf("%d", &choice);
            getchar();

        switch (choice) {
            case 1:
                printf("Nyuszi neve: ");
	       	 scanf(" %[^\n]", bunnies[i].name);
                break;
            case 2:
                printf("Nyuszi verse: ");
	       	 scanf(" %[^\n]", bunnies[i].vers);
                break;
            case 3:
                printf("Nyuszi tojásainak száma: ");
	       	 scanf(" %d", &bunnies[i].eggs);
                break;
            default:
                printf("Érvénytelen opció!\n");
	}
            
            file = fopen(FILENAME, "w");
            for (int j = 0; j < count; j++) {
                fprintf(file, "%s|%s|%d\n", bunnies[j].name, bunnies[j].vers, bunnies[j].eggs);
            }
            fclose(file);
            printf("Módosítás sikeres!\n");
            return;
        }
    }
    printf("Nem található ilyen nevű nyuszi!\n");
}

void deleteBunny() {
    FILE *file = fopen(FILENAME, "r");
    if (!file) {
        printf("Nincsenek regisztrált nyuszik.\n");
        return;
    }

    Bunny bunnies[100];
    int count = 0;
    char line[1500];
    while(fgets(line,sizeof(line),file)){
	strncpy(bunnies[count].name,strtok(line, "|"),MAX_NAME);
	strncpy(bunnies[count].vers,strtok(NULL, "|"),MAX_VERS);
       bunnies[count].eggs = atoi(strtok(NULL, "|"));
	count+=1;
    }
    fclose(file);
    	
    char name[MAX_NAME];
    printf("Melyik nyuszit szeretnéd törölni? ");
    scanf(" %[^\n]", name);
    
    file = fopen(FILENAME, "w");
    for (int i = 0; i < count; i++) {
        if (strcmp(bunnies[i].name, name) != 0) {
            fprintf(file, "%s|%s|%d\n", bunnies[i].name, bunnies[i].vers, bunnies[i].eggs);
        }
    }
    fclose(file);
    printf("Nyuszi törölve!\n");
}




int main() {
    int choice;
    do {
        printf("\n1. Jelentkezés a versenyre\n");
        printf("2. Nyuszik listázása\n");
        printf("3. Adatok módosítása\n");
        printf("4. Nyuszi törlése\n");
        printf("5. Kilépés\n");
        printf("Válassz egy opciót: ");
        scanf("%d", &choice);
        getchar();

        switch (choice) {
            case 1:
                registerBunny();
                break;
            case 2:
                listBunnies();
                break;
            case 3:
                modifyBunny();
                break;
            case 4:
                deleteBunny();
                break;
            case 5:
                printf("Kilépés...\n");
                break;
            default:
                printf("Érvénytelen opció!\n");
        }
    } while (choice != 5);
    return 0;
}