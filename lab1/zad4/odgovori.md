# Zadatak 4.

## Odgovor na 1. pitanje
Objekt `poc` se alocira na programskom stogu prilikom pozivanja `PlainOldClass::set` u `main`.
`rbp` je registar za referencu na stog.

```assembly
	lea	rax, -36[rbp]
	mov	esi, 42
	mov	rdi, rax
	call	_ZN13PlainOldClass3setEi
```

Objekt `*pb` se alocira na gomili prilikom pozivanja operatora `new` u `main`.
Sam pointer `pb` se alocira na programskom stogu.
To se vidi iz oznake `QWORD PTR`.

```assembly
	mov	edi, 16
	call	_Znwm@PLT
	mov	rbx, rax
	mov	rdi, rbx
	call	_ZN9CoolClassC1Ev
	mov	QWORD PTR -32[rbp], rbx
```

## Odgovor na 2. pitanje

Zanimljivo je što naredba `PlainOldClass poc;` nema analognu liniju u asemblerskom kodu, već se `poc` alocira prilikom poziva `poc.set(42);`.
Objekt se kao takav alocira na stogu, pa se stoga alocira tek kada se koristi.

S druge strane, `*pb` koristi operator `new` što znači da će se objekt na koji pointer pokazuje alocirati na gomili.
To se događa u trenutku poziva `new` te poziva konstruktora klase `CoolClass`: `call _ZN9CoolClassC1Ev`.

## Odgovor na 3. pitanje
Konstruktor za `poc` ne postoji jer klasa `PlainOldClass` nema virtualnih metoda.

## Ogovor na 4. pitanje

```assembly
	mov	edi, 16
	call	_Znwm@PLT
	mov	rbx, rax
	mov	rdi, rbx
	call	_ZN9CoolClassC1Ev
	mov	QWORD PTR -32[rbp], rbx
```

`_Znwm@PLT` reprezentira operator `new` koji stvara novi objekt na gomili.
Veličina objekta je 16 bajtova što se vidi iz `mov	edi, 16`.
Nakon toga se na temelju povratnih vrijednosti poziva `_ZN9CoolClassC1Ev` što je kontruktor klase `CoolClass`.
Konstruktor vraća pointer na objekt koji se sprema iz registra `rbx` na stog.

## Ogovor na 5. pitanje

```assembly
	lea	rax, -36[rbp]
	mov	esi, 42
	mov	rdi, rax
	call	_ZN13PlainOldClass3setEi
```

Poziv `poc.set` relativno je jednostavan.
U `esi` se pohranjuje argument poziva, dakle broj `42`.
U `rdi` se pohranjuje adresa objekta na kojem se poziva metoda.
Metoda nije virtualna, pa se poziva direktno preko njene adrese, što vidimo u instrukciji `call`.
Zbog toga što se motoda poziva direktno po adresi, moguće ju je prevesti inline.

```assembly
	mov	rax, QWORD PTR -32[rbp]
	mov	rax, QWORD PTR [rax]
	mov	rdx, QWORD PTR [rax]
	mov	rax, QWORD PTR -32[rbp]
	mov	esi, 42
	mov	rdi, rax
	call	rdx
```

Poziv `pb->set` nešto je kompliciraniji.
Registar `-32[rbp]`, u kojem se nalazi `pb`, pohranjuje se u `rax`.
Zatim se dinamički iz virtualne tablice pronalazi odgovarajuća metoda koja se sprema u `rdx`.
U `esi` se pohranjuje argument, a u `rdi` objekt.
Instrukcija `call` poziva adresu iz registra jer je metoda virtualna.
Zbog dinačkog pozivanja nije moguće koristiti inlining.

## Odgovor na 6. pitanje

```assembly
_ZTV9CoolClass:
	.quad	0
	.quad	_ZTI9CoolClass
	.quad	_ZN9CoolClass3setEi
	.quad	_ZN9CoolClass3getEv
```

Ovdje se vidi struktura virtualne tablice `CoolClass`.
Sadrži pointere na `set` i `get` metode.

```assembly
	call	_ZN4BaseC2Ev
	lea	rdx, _ZTV9CoolClass[rip+16]
	mov	rax, QWORD PTR -8[rbp]
```

Tablica se referencira u kontruktoru `CoolClass`, nakon poziva konstruktora `Base`.