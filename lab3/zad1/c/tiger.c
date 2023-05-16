#include <stdlib.h>

typedef char const *(*PTRFUN)();

struct Tiger
{
    PTRFUN *vtable;
    char const *name;
} typedef Tiger;

char const *name(void *this)
{
    return ((Tiger *)this)->name;
}

char const *greet()
{
    return "rrr";
}

char const *menu()
{
    return "antelope";
}

PTRFUN vTiger[3] = {name, greet, menu};

size_t size()
{
    return (sizeof(struct Tiger));
}

void *create(char const *name)
{
    Tiger *tiger = (Tiger *)malloc(sizeof(Tiger));
    tiger->name = name;
    tiger->vtable = vTiger;
    return (void *)tiger;
}

void *construct(void *obj, char const *name)
{
    Tiger *tiger = (Tiger *)obj;
    tiger->name = name;
    tiger->vtable = vTiger;
    return (void *)tiger;
}