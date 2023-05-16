build:
	gcc main.c myfactory.c -ldl
	gcc -shared -fPIC tiger.c -o tiger.so
	gcc -shared -fPIC parrot.c -o parrot.so

run:
	./a.out tiger Mirko parrot Modrobradi