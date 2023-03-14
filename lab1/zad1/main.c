#include <stdlib.h>
#include <stdio.h>

char const *dogGreet(void)
{
    return "vau!";
}
char const *dogMenu(void)
{
    return "kuhanu govedinu";
}
char const *catGreet(void)
{
    return "mijau!";
}
char const *catMenu(void)
{
    return "konzerviranu tunjevinu";
}

typedef char const *(*PTRFUN)();

typedef struct vAnimal
{
    PTRFUN animalGreet;
    PTRFUN animalMenu;
} vAnimal;

typedef struct Animal
{
    const char *name;
    const vAnimal *vtable;
} Animal;

const vAnimal vDog = {
    .animalGreet = dogGreet,
    .animalMenu = dogMenu,
};

const vAnimal vCat = {
    .animalGreet = catGreet,
    .animalMenu = catMenu,
};

void animalPrintGreeting(Animal const *obj)
{
    printf("%s pozdravlja: %s\n", obj->name, obj->vtable->animalGreet());
}

void animalPrintMenu(Animal const *obj)
{
    printf("%s voli %s\n", obj->name, obj->vtable->animalMenu());
}

void constructDog(Animal *obj, const char *name) {
    obj->name = name;
    obj->vtable = &vDog;
    return;
}

Animal *createDog(const char *name)
{
    Animal *obj = (Animal *) malloc(sizeof(Animal));
    constructDog(obj, name);
    return obj;
}

void constructCat(Animal *obj, const char *name) {
    obj->name = name;
    obj->vtable = &vCat;
    return;
}

Animal *createCat(const char *name)
{
    Animal *obj = (Animal *) malloc(sizeof(Animal));
    constructCat(obj, name);
    return obj;
}

void testAnimals(void)
{
    struct Animal *p1 = createDog("Hamlet");
    struct Animal *p2 = createCat("Ofelija");
    struct Animal *p3 = createDog("Polonije");

    animalPrintGreeting(p1);
    animalPrintGreeting(p2);
    animalPrintGreeting(p3);

    animalPrintMenu(p1);
    animalPrintMenu(p2);
    animalPrintMenu(p3);

    free(p1);
    free(p2);
    free(p3);
}

int main()
{
    testAnimals();
}