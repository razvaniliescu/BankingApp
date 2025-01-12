# Project Assignment POO  - J. POO Morgan - Phase Two

## Structura proiectului

* core/ - elementele de baza ala aplicatiei
  * user/ - pachet ce contine clasa utilizatorilor si a service planurilor
    * User - clasa ce contine campuri si metode pentru definirea utilizatorului, cum ar fi adaugarea si stergerea conturilor, adaugarea tranzactiilor si gestionarea aliasurilor
    * ServicePlan - clasa ce contine un enum pentru tipurile de plan si metode ce prelucreaza elementele acestui enum
  * accounts/ - in acest pachet am pus cele doua tipuri de conturi:
    * Account - contul clasic. Pe langa campurile mentionate in enunt, contul mai are si trei liste de tranzactii, una pentru toate tipurile de tranzactii, una doar pentru cele online, lucru ce face mult mai usoara generarea de rapoarte, si una pentru a tine evidenta depositelor
    * SavingsAccount - contul de economii. Fata de contul clasic, are in plus un camp de dobanda
    * BusinessAccount - contul de business. Fata de contul clasic, are in plus doua seturi de useri, pentru angajati si manageri, si doua campuri de double, pentru spending limit si deposit limit. Pentru acest cont, campul de user este chiar owner-ul contului
  * cards/ - pachet pentru cele doua tipuri de card care pot exista. Fiecare clasa are si o metoda pay care actualizeaza cardul cand se face o plata
    * Card - clasa pentru cardul clasic. Aici, metoda pay nu face nimic
    * OneTimeCard - subclasa pentru cardul one-time. Metoda pay reface cardul si adauga doua tranzactii in contul utilizatorului
  * exchange - pachet pentru elemente legate de schimbul valutar
    * ExchangeRate - clasa folosita pentru a prelua elementele din input
    * ExchangeGraph - aceasta clasa contine un hashmap din care se pot extrage toate ratele de schimb valutar. Clasa contine o metoda de completare a structurii pe baza datelor din input si o metoda de extragere a datelor
* commands/ - pachet ce contine toate comenzile aplicatiei
  * Command - command design pattern. Reprezinta o clasa abstracta ce contine campurile de command si timestamp si o metoda execute, care primeste ca parametri un objectMapper, un arrayNode (output-ul), lista de utilizatori, lista de comercianti si graful de schimburi valutare. Toate comenzile mostenesc aceasta clasa si sunt invocate in main
  * CommandFactory - factory design pattern pentru crearea de comenzi. Contine o metoda statica ce verifica numele dat in input si returneaza comanda corespunzatoare.
  * commandTypes - pachet ce contine toate tipurile de comenzi
    * account - comenzi ce tin de conturi
      * AddAccount - creeaza un cont nou in functie de tipul specificat
      * DeleteAccount - sterge contul specificat daca acesta nu mai are fonduri
      * SetAlias - seteaza un alias pentru un alt cont
      * SetMinBalance - seteaza balance-ul minim pentru contul specificat. In cazul contului de business, doar detinatorul contului poate efectua aceasta operatie
      * UpgradePlan - imbunatateste planul utilizatorului pentru toate conturile sale, plate efectuandu-se din contul specificat
      * business - comenzi ce tin de contul de business
        * AddNewBusinessAssociate - adauga un now asociat la un cont de business cu functia specificata. Daca utilizatorul deja este asociat contului, acesta nu este adaugat din nou
        * ChangeDepositLimit - schimba limita de deposit pentru angajati. Doar detinatorul contului poate da aceasta comanda
        * ChangeSpendingLimit - schimba limita de cheltuit pentru angajati. Doar detinatorul contului poate da aceasta comanda
      * savings - comenzi ce tin de contul de savings
        * AddInterest - adauga fonduri in cont in functie de dobanda si de capitalul curent
        * ChangeInterestRate - schimba dobanda unui cont de savings
        * WithdrawSavings - extrage fonduri din contul de savings specificat si le pune in primul cont clasic ce are moneda data
    * card - comenzi ce tin de carduri
      * CheckCardStatus - verifica soldul curent al contului. Daca este sub limita setata, cardul este inghetat
      * CreateCard - creeaza un card nou pentru contul specificat
      * CreateOneTimeCard - creeaza un card one-time nou pentru contul specificat
      * DeleteCard - sterge cardul specificat in input
    * debug - comenzi pentru afisarea starii curente a aplicatiei
      * PrintUsers - afiseaza informatii despre toti utilizatorii, inclusiv conturile acestora
      * PrintTransactions - afiseaza toate tranzactiile unui utilizator
    * payments - comenzi legate de plati
      * AddFunds - adauga fonduri in contul specificat. In cazul contului de business, se verifica functia pe care o are utilizatorul care da comanda
      * CashWithdrawal - extrage bani din cont in RON
      * PayOnline - plateste online la un comerciant, aplicandu-se atat comisionul cat si cashbackul corespunzator
      * SendMoney - trimite bani la alt cont, aplicandu-se comisionul corespunzator. Daca contul in care se trimit banii este al unui comerciant, comanda se comporta ca payOnline
      * splitPayment - comenzi legate de plata distribuita
        * AcceptSplitPayment - accepta urmatoarea plata de acelasi tip ca cel dat. Daca nu mai sunt utilizatori care trebuie sa accepte, se efectueaza plata
        * RejectSplitPayment - accepta urmatoarea plata de acelasi tip ca cel dat. Plata se va sterge din conturile tuturor celor implicati si se va adauga o tranzactie corespunzatoare
        * SplitPayment - metoda execute adauga plata in conturile specificate. Tot aici sunt si metodele ce proceseaza platile acceptate in totalitate sau respinse
      * reports - comenzi ce tin de rapoarte
        * BusinessReport - creeaza un raport pentru contul de business care poate fi de 2 tipuri:
          * transaction - urmareste toate tranzactiile si depositele facute in perioada de timp specificata si creeaza 2 mapuri, unul pentru angajati si unul pentru manageri, ce leaga fiecare utilizator de sumele cheltuite si adaugate
          * commerciant - urmareste toate platile facute la fiecare comerciant si cine a facut acele plati
        * Report - afiseaza tranzactiile de pe un cont pe o perioada de timp
        * SpendingReport - afiseaza sumele cheltuite la fiecare comerciant pe o perioada de timp
* commerciants/ - pachet pentru aciuni legate de comercianti
  * Commerciant - clasa ce contine informatii despre un comerciant
  * CashbackDetails - clasa ce contine informatii despre statusul cashbackului unui utilizator: cat a cheltuit in total la comercianti de tipul spendingThreshold , cate tranzactii a facut la fiecare comerciant de tipul nrOfTransactions si daca a primit un cashback de acest tip deja.
  * CashbackStrategy - strategy design pattern pentru functionalitatea de cashback. Aceasta interfata contine o metoda care este apelata in cele doua comenzi care pot beneficia de cashback, sendMoney si payOnline. Tot in cele 2 comenzi este si setata strategia de cashback in functie de tipul comerciantului.
    * SpendingThreshold - verifica suma platita si planul contului si adauga cashback-ul in cont
    * NrOfTransactions - seteaza procentul cashback pentru urmatoarea tranzactie in functie de numarul de tranzactii facute la un comerciant 
* transactions/ - pachet pentru tranzactii
  * Transaction - fata de etapa precedenta, am renuntat la a face o clasa noua pentru fiecare tranzactie si am folosit un builder design pattern. Astfel, aceasta clasa contine toate campurile posibile pe care le poate avea o tranzactie si chiar cateva in plus, cum ar fi deposit sau format, pentru a usura anumite operatii. Clasa contine si o metoda de print care afiseaza in format JSON tranzactia in functie de campurile setate.
* exceptions/ - pachet pentru exceptii
  * MyException - clasa ce mosteneste Exception, doar ca mai are in plus o functie de printare a acesteia in format JSON. Am mai creat o clasa separata pentru un tip de exceptie in care am suprascris metoda, deoarece acea eroare cere alt format decat cel obisnuit
* Main - datorita modului in care am implementat comenzile, main-ul este destul de scurt: resetez seed-urile, preiau datele utilizatorilor, comerciantii si informatiile despre schimbul valutar, dupa care creez comenzile folosind un factory si le execut 



