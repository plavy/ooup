#include <stdio.h>
#include <stdlib.h>

#include "myfactory.h"

typedef char const *(*PTRFUN)();

struct Animal
{
    PTRFUN *vtable;
    // vtable entries:
    // 0: char const* name(void* this);
    // 1: char const* greet();
    // 2: char const* menu();
} typedef Animal;

char const *animalName(Animal *obj)
{
    return obj->vtable[0](obj);
}

char const *animalPrintGreeting(Animal *obj)
{
    printf("%s says %s!\n", animalName(obj), obj->vtable[1]());
    return "";
}

char const *animalPrintMenu(Animal *obj)
{
    printf("%s eats %s\n", animalName(obj), obj->vtable[2]());
    return "";
}

PTRFUN vAnimal[3] = {animalName, animalPrintGreeting, animalPrintMenu};

int main(int argc, char *argv[])
{
    if (0) // 1 for heap, 0 for stack 
    {
        for (int i = 0; i < argc / 2; ++i)
        {
            struct Animal *p = (struct Animal *)myfactory(argv[1 + 2 * i], argv[1 + 2 * i + 1]);
            if (!p)
            {
                printf("Creation of plug-in object %s failed.\n", argv[1 + 2 * i]);
                continue;
            }

            animalPrintGreeting(p);
            animalPrintMenu(p);
            free(p);
        }
    }
    else
    {
        for (int i = 0; i < argc / 2; ++i)
        {
            char obj[myfactory_size(argv[1 + 2 * i])];
            myfactory_construct(obj, argv[1 + 2 * i], argv[1 + 2 * i + 1]);
            struct Animal *p = (Animal *)&obj;
            if (!p)
            {
                printf("Creation of plug-in object %s failed.\n", argv[1 + 2 * i]);
                continue;
            }

            animalPrintGreeting(p);
            animalPrintMenu(p);
        }
    }
}