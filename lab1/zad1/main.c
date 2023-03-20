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

const vAnimal vDog = {
    .animalGreet = dogGreet,
    .animalMenu = dogMenu,
};

const vAnimal vCat = {
    .animalGreet = catGreet,
    .animalMenu = catMenu,
};

typedef struct Animal
{
    const char *name;
    const vAnimal *vtable;
} Animal;

void animalPrintGreeting(Animal const *obj)
{
    printf("%s pozdravlja: %s\n", obj->name, obj->vtable->animalGreet());
}

void animalPrintMenu(Animal const *obj)
{
    printf("%s voli %s\n", obj->name, obj->vtable->animalMenu());
}

void constructDog(Animal *obj, const char *name)
{
    obj->name = name;
    obj->vtable = &vDog;
    return;
}

Animal *createDog(const char *name)
{
    Animal *obj = (Animal *)malloc(sizeof(Animal));
    constructDog(obj, name);
    return obj;
}

void constructCat(Animal *obj, const char *name)
{
    obj->name = name;
    obj->vtable = &vCat;
    return;
}

Animal *createCat(const char *name)
{
    Animal *obj = (Animal *)malloc(sizeof(Animal));
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

Animal *create_n_dogs(int n)
{
    Animal *obj = (Animal *)malloc(sizeof(Animal) * n);
    for (int i = 0; i < n; i++)
    {
        constructDog(obj + (i * sizeof(Animal)), "Doge");
    }
    return obj;
}

int main()
{
    testAnimals();

    Animal stack_dog;
    constructDog(&stack_dog, "Stack dog");
    animalPrintGreeting(&stack_dog);

    Animal *heap_dog = (Animal *)malloc(sizeof(Animal));
    constructDog(heap_dog, "Heap dog");
    animalPrintGreeting(heap_dog);

    Animal *first_dog = create_n_dogs(4);
    animalPrintGreeting(first_dog + 3 * sizeof(Animal));
    
    printf("Size of Animal: %ld\n", sizeof(Animal));
}