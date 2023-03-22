#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

// Unary_Function
typedef struct Unary_Function
{
    const struct vUnary_Function *vtable;
    int lower_bound;
    int upper_bound;
} Unary_Function;

typedef double (*PTRFUN)(Unary_Function *, double);

typedef struct vUnary_Function
{
    // double (*value_at)(struct Unary_Function *, double);
    // double (*negative_value_at)(struct Unary_Function *, double);
    PTRFUN value_at;
    PTRFUN negative_value_at;
} vUnary_Function;

double value_at(Unary_Function *f, double x)
{
    printf("ERROR Pure virtual function called!\n");
    exit;
}

double negative_value_at(Unary_Function *f, double x)
{
    return -(f->vtable->value_at(f, x));
}

void tabulate(Unary_Function *f)
{
    for (int x = f->lower_bound; x <= f->upper_bound; x++)
    {
        printf("f(%d)=%lf\n", x, f->vtable->value_at(f, x));
    }
};

bool same_functions_for_ints(Unary_Function *f1, Unary_Function *f2, double tolerance)
{
    if (f1->lower_bound != f2->lower_bound)
        return false;
    if (f1->upper_bound != f2->upper_bound)
        return false;
    for (int x = f1->lower_bound; x <= f1->upper_bound; x++)
    {
        double delta = f1->vtable->value_at(f1, x) - f2->vtable->value_at(f2, x);
        if (delta < 0)
            delta = -delta;
        if (delta > tolerance)
            return false;
    }
    return true;
};

const vUnary_Function vTableUnary_Function = {
    .value_at = value_at,
    .negative_value_at = negative_value_at,
};

void constructUnary_Function(Unary_Function *obj, int lb, int ub)
{
    obj->lower_bound = lb;
    obj->upper_bound = ub;
    obj->vtable = &vTableUnary_Function;
    return;
}

// Square
typedef struct Square
{
    Unary_Function super;
} Square;

typedef struct vSquare
{
    PTRFUN value_at;
    PTRFUN negative_value_at;
} vSquare;

double square_value_at(Unary_Function *f, double x)
{
    return x * x;
}

const vSquare vTableSquare = {
    .value_at = square_value_at,
    .negative_value_at = negative_value_at,
};

void constructSquare(Square *obj, int lb, int ub)
{
    constructUnary_Function(&(obj->super), lb, ub);
    obj->super.vtable = (vUnary_Function *)&vTableSquare;
    return;
}

// Linear
typedef struct Linear
{
    Unary_Function super;
    double a;
    double b;
} Linear;

typedef struct vLinear
{
    PTRFUN value_at;
    PTRFUN negative_value_at;
} vLinear;

double linear_value_at(Unary_Function *f, double x)
{
    Linear *l = (Linear *)f;
    return l->a * x + l->b;
}

const vLinear vTableLinear = {
    .value_at = linear_value_at,
    .negative_value_at = negative_value_at,
};

void constructLinear(Linear *obj, int lb, int ub, double a, double b)
{
    constructUnary_Function(&(obj->super), lb, ub);
    obj->a = a;
    obj->b = b;
    obj->super.vtable = (vUnary_Function *)&vTableLinear;
    return;
}

// Main
int main()
{
    Square *sq = (Square *)malloc(sizeof(Square));
    constructSquare(sq, -2, 2);
    Unary_Function *f1 = (Unary_Function *)sq;
    tabulate(f1);
    Linear lin;
    constructLinear(&lin, -2, 2, 5, -2);
    Unary_Function *f2 = (Unary_Function *)&lin;
    tabulate(f2);
    printf("f1==f2: %s\n", same_functions_for_ints(f1, f2, 1E-6) ? "DA" : "NE");
    printf("neg_val f2(1) = %lf\n", f2->vtable->negative_value_at(f2, 1.0));
    printf("sizeof Unary_Function = %ld\n", sizeof(Unary_Function));
    printf("sizeof Square = %ld\n", sizeof(Square));
    printf("sizeof Linear = %ld\n", sizeof(Linear));
    return 0;
}
