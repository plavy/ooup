#include <stdlib.h>

typedef char const *(*PTRFUN)();

struct Parrot
{
    PTRFUN *vtable;
    char const *name;
} typedef Parrot;

char const *name(void *this)
{
    return ((Parrot *)this)->name;
}

char const *greet()
{
    return "hi";
}

char const *menu()
{
    return "flower";
}

PTRFUN vParrot[3] = {name, greet, menu};

size_t size()
{
    return (sizeof(struct Parrot));
}

void *create(char const *name)
{
    Parrot *parrot = (Parrot *)malloc(sizeof(Parrot));
    parrot->name = name;
    parrot->vtable = vParrot;
    return (void *)parrot;
}

void *construct(void *obj, char const *name)
{
    Parrot *parrot = (Parrot *)obj;
    parrot->name = name;
    parrot->vtable = vParrot;
    return (void *)parrot;
}