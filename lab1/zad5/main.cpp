#include <stdio.h>

class B
{
public:
    virtual int prva() = 0;
    virtual int druga(int) = 0;
};

class D : public B
{
public:
    virtual int prva() { return 42; }
    virtual int druga(int x) { return prva() + x; }
};

void manual_call(B *pb) {
    unsigned long long vtblAddress = *(unsigned long long *)pb; // 64-bit pointer

    typedef int (*PTRFUN1)(B *);
    PTRFUN1 prvaF = (PTRFUN1)(*(unsigned long long *)(vtblAddress)); // first 64-bit pointer
    printf("Prva: %d\n", prvaF(pb));

    typedef int (*PTRFUN2)(B *, int);
    PTRFUN2 drugaF = (PTRFUN2)(*(unsigned long long *)(vtblAddress + 8)); // second 64-bit pointer
    printf("Druga: %d\n", drugaF(pb, 42));
}

int main() {
    B *obj = new D;
    manual_call(obj);
}