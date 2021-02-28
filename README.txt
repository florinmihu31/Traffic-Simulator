	Mihu Florin - 334CC
	Tema 2 - APD

	1. Simple Semaphore
	Fiecare masina asteapta un numar de milisecunde prestabilit, cu ajutorul 
functiei sleep, inainte de a afisa mesajul final.

	2. Simple N Roundabout
	Fiecare masina cand ajunge la giratoriu decrementeaza semaforul, afiseaza
mesajul, apoi sta in sleep roundaboutTime milisecunde. La iesire, acestea cresc 
semaforul pentru a permite altor masini sa intre.

	3. Simple Strict 1 Car Roundabout
	Fiecare banda are semaforul ei propriu, initializat cu valoarea 1. Cand o 
masina ajunge la giratoriu, aceasta incearca sa decrementeze valoarea
semaforului corespunzator benzii. Dupa ce iese din giratoriu dupa 
roundaboutTime milisecunde, masina elibereaza semaforul.

	4. Simple Strict X Car Roundabout
	Cu ajutorul unei bariere de lungime carsNo, se asteapta ca toata masinile 
sa ajunga la giratoriu. Dupa aceea, fiecare masina incearca sa decrementeze 
semaforul corespunzator benzii sale. Fiecare masina apoi asteapta la o bariera 
de capacitate (roundaboutCars * directionsNo), pentru ca masinile sa fie 
selectate pe fiecare sens. Masinile intra in giratoriu, asteapta roundaboutTime 
milisecunde, iar la iesire asteapta la o noua bariera pentru ca toate masinile 
selectate sa iasa in acelasi timp. La final, fiecare masina incrementeaza 
semaforul benzii sale.

	5. Simple Max X Car Roundabout
	Cand o masina ajunge la giratoriu incearca sa decrementeze semaforul benzii 
sale. Daca masina a intrat in giratoriu, asteapta roundaboutTime milisecunde si 
iese din giratoriu, eliberand semaforul directiei sale.

	6. Priority Intersection
	Daca masina este cu prioritate se verifica daca coada masinilor cu 
prioritate este goala. In caz afirmativ, se blocheaza accesul in intersectie a 
masinilor fara prioritate, cu ajutorul unui semafor binar. Se adauga masina in 
coada, masina intra in intersectie, sta 2000 milisecunde si apoi iese, fiind 
scoasa din coada. In cazul in care coada de masini cu prioritate ramane goala, 
se deblocheaza intersectia pentru masinile fara prioritate, incrementand 
semaforul.
	Daca masina nu are prioritate, dupa ce este adaugata in coada, ea incearca 
sa intre in intersectie accesand semaforul, insa daca aceasta nu este goala si 
nu este prima masina din coada de masini fara prioritate aceasta nu poate 
intra. Dupa ce a intrat in intersectie, afiseaza mesajul corespunzator, iar 
apoi este scoasa din coada. La final se elibereaza si semafoarele blocate de 
aceasta.

	7. Crosswalk
	Pentru acest exercitiu se instantiaza clasa de pietoni cu timpul de 
executie si numarul de pietoni dat in fisier. Cat timp nu au trecut toti 
pietonii (metoda isFinished) masinile primesc cate un mesaj referitor la 
culoarea semaforului. Daca nu trec, pietoni atunci masinile primesc mesaj ca au 
verde, in caz contrar primesc mesajul ca au rosu la semafor.

	8. Simple Maintenance
	Se initilizeaza doua semafoare pentru cele doua sensuri. Pentru sensul 0, 
semaforul are valoarea egala cu numarul de masini ce pot trece pe sens, iar 
celalalt are valoarea 0. Fiecare masina incearca sa intre pe portiunea in 
reparatie, incercand sa decrementeze semaforul. Dupa ce trec de portiune, 
threadurile asteapta la o bariera. Daca toate au ajuns la bariera, fiecare 
masina incrementeaza valoarea semaforului celuilalt sens.

	10. Railroad
	Fiecare sens are o coada pentru a retine ordinea masinilor de pe acesta. 
Cand o masina ajunge la calea ferata, ea este adaugata in coada. Threadurile 
asteapta la doua bariere, una inainte de afisare si una dupa afisarea mesajului 
ca trenul a trecut. Doar threadul cu id-ul 0 afiseaza mesajul ca trenul a 
trecut. La final masinile sunt scoate din coada in ordinea in care au fost 
introduse, astfel incat fiecare thread asteapta ca id-ul lui sa fie primul in 
coada inainte de a afisa ca a trecut calea ferata. La final, se scoate masina 
din coada.
