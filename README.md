# Project Assignment POO  - J. POO Morgan - Phase One

## Structura proiectului

* core/ - elementele de baza ala aplicatiei
  * User - clasa ce contine campuri si metode pentru definirea utilizatorului, cum ar fi adaugarea si stergerea conturilor, adaugarea tranzactiilor si gestionarea aliasurilor
  * accounts/ - in acest pachet am pus cele doua tipuri de conturi:
    * Account - contul clasic. Pe langa campurile mentionate in enunt, contul mai are si doua liste de tranzactii, una pentru toate tipurile de tranzactii si alta doar pentru cele online, lucru ce face mult mai usoara generarea de rapoarte
    * SavingsAccount - contul de economii. Fata de contul clasic, are in plus un camp de dobanda
  * cards/ - pachet pentru cele doua tipuri de card care pot exista. Fiecare clasa are si o metoda pay care actualizeaza cardul cand se face o plata
    * Card - clasa pentru cardul clasic. Aici, metoda pay nu face nimic
    * OneTimeCard - subclasa pentru cardul one-time. Metoda pay reface cardul si adauga doua tranzactii in contul utilizatorului
  * exchange - pachet pentru elemente legate de schimbul valutar
    * ExchangeRate - clasa folosita pentru a prelua elementele din input
    * ExchangeGraph - aceasta clasa contine un hashmap din care se pot extrage toate ratele de schimb valutar. Clasa contine o metoda de completare a structurii pe baza datelor din input si o metoda de extragere a datelor
* commands/ - pachet ce contine toate comenzile aplicatiei
  * Command - clasa abstracta ce contine campurile de command si timestamp si o metoda execute, care primeste ca parametri un objectMapper, un arrayNode (output-ul), lista de utilizatori si graful de schimburi valutare. Toate comenzile mostenesc aceasta clasa
  * CommandFactory - factory design pattern pentru crearea de comenzi. Contine o metoda statica ce verifica numele dat in input si returneaza comanda corespunzatoare.
* transactions/ - pachet pentru tranzactii
  * Transaction - clasa pentru o tranzactie de baza ce contine doar un timestamp, descrierea si o functie de printare in format JSON. Toate clasele din acest pachet mostenesc aceasta clasa. Am ales sa nu fac o clasa abstracta, deoarece am facut cate o clasa pentru fiecare tip de tranzactie, iar multe aveau nevoie de aceeasi functie de printare
  * error/ - pachet pentru tranzactiile afisate in cazul in care o plata nu s-a putut desfasura
  * success/ - pachet pentru tranzactiile afisate in cazul in care o plata s-a efectuat cu succes
* exceptions/ - pachet pentru exceptii
  * MyException - clasa ce mosteneste Exception, doar ca mai are in plus o functie de printare a acesteia in format JSON. Toate celelalte clase din pachet mostenesc aceasta clasa
* Main - datorita modului in care am implementat comenzile, main-ul este destul de scurt: resetez seed-urile, preiau datele utilizatorilor si informatiile despre schimbul valutar, dupa care creez comenzile folosind un factory si le execut 



