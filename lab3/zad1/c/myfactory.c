#include <dlfcn.h>
#include <string.h>

#include "myfactory.h"

void *get_handle(char const *libname)
{
    char filename[100] = "./";
    strcat(filename, libname);
    strcat(filename, ".so");
    return dlopen(filename, RTLD_LAZY);
}

void *myfactory(char const *libname, char const *ctorarg)
{

    typedef void *(*PTRCREATE)(const char *);
    PTRCREATE create = dlsym(get_handle(libname), "create");
    return create(ctorarg);
}

size_t myfactory_size(char const *libname)
{
    typedef size_t (*PTRSIZE)();
    PTRSIZE size = dlsym(get_handle(libname), "size");
    return size();
}

void *myfactory_construct(void *obj, char const *libname, char const *ctorarg)
{
    typedef void *(*PTRCONSTRUCT)(void *, const char *);
    PTRCONSTRUCT construct = dlsym(get_handle(libname), "construct");
    construct(obj, ctorarg);
    return obj;
}