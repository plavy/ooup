#include <stdio.h>

class CoolClass
{
public:
    virtual void set(int x) { x_ = x; };
    virtual int get() { return x_; };

private:
    int x_;
};

class PlainOldClass
{
public:
    void set(int x) { x_ = x; };
    int get() { return x_; };

private:
    int x_;
};

int main()
{
    // 64-bitna architektura -> jedan pointer ~ 8 bajtova
    printf("Size of pointer: %ld\n", sizeof(char *));

    // Bez virtualnih metoda, samo jedan int od 4 bajta
    printf("Size of PlainOldClass: %ld\n", sizeof(PlainOldClass));
    
    // Ima virtualne metode -> jedan pointer na virtualnu tablicu ~ 8 bajtova
    // Dodatni int ~ 8 bajtova umjesto 4 zbog paddinga (za lakše poravnavanje polja)
    printf("Size of CoolClass: %ld\n", sizeof(CoolClass));
}