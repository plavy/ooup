# Zadatak 6.

## Odgovor na pitanje

U `main` metodu instancira se objekt razreda `Derived` na gomili.
`Derived` u svom kontruktoru poziva `metoda()`.
Implicitno, prije poziva `metoda()`, poziva se konstruktor razreda `Base`.

```assembly
    mov	QWORD PTR -8[rbp], rdi
	mov	rax, QWORD PTR -8[rbp]
	mov	rdi, rax                    // postavljanje pointera na objekt
	call	_ZN4BaseC2Ev            // Base konstruktor
	lea	rdx, _ZTV7Derived[rip+16]   // postavljanje vtable Derived
	mov	rax, QWORD PTR -8[rbp]
	mov	QWORD PTR [rax], rdx
	mov	rax, QWORD PTR -8[rbp]
	mov	rdi, rax                    // postavljanje pointera na objekt
	call	_ZN4Base6metodaEv       // metoda()
```

Kao što se vidi, `Base` konstruktor poziva se i prije postavljanja virtualne tablice.
`Base` konstruktor zatim postavlja svoju vlastitu virtualnu tablicu te poziva `metoda()`. 

```assembly
    mov	QWORD PTR -8[rbp], rdi
	lea	rdx, _ZTV4Base[rip+16]  // postavljanje vtable Base
	mov	rax, QWORD PTR -8[rbp]
	mov	QWORD PTR [rax], rdx
	mov	rax, QWORD PTR -8[rbp]
	mov	rdi, rax                // postavljanje pointera na objekt
	call	_ZN4Base6metodaEv   // metoda()
```

`metoda()` iz Base zatim poziva `virtualnaMetoda()`.
Obzirom da vtable trenutačno pokazuje na metodu iz `Base`, ona se poziva.

Poslije toga, izvršavanje se vraća u `Derived` konstruktor, gdje se postavlja njegova vtable, te poziva njegova `virtualnaMetoda()`.

Potom, se `metoda()` poziva još jednom u `main` metodi.
Sada se opet poziva `virtualnaMetoda()` iz `Derived` jer je vtable već ispravno postavljen.