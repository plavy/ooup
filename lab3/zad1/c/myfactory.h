void* myfactory(char const* libname, char const* ctorarg);

size_t myfactory_size(char const* libname);
void* myfactory_construct(void* obj, char const* libname, char const* ctorarg);
