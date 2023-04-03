#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int gt_int(const void *a, const void *b)
{
    if (*(int *)a > *(int *)b)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

int gt_char(const void *a, const void *b)
{
    if (*(char *)a > *(char *)b)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

int gt_str(const void *a, const void *b)
{
    if (strcmp(*((char **)a), *((char **)b)) > 0)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

const void *mymax(
    const void *base, size_t nmemb, size_t size,
    int (*compar)(const void *, const void *))
{
    int i_max = 0;
    for (int i = 1; i < nmemb; ++i)
    {
        if (compar((void *)(base + i * size), (void *)(base + i_max * size)) == 1)
        {
            i_max = i;
        }
    }
    return (base + i_max * size);
}

int main()
{
    int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
    char arr_char[] = "Suncana strana ulice";
    const char *arr_str[] = {
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"};

    const void *el_max = mymax(arr_int, 9, sizeof(int), &gt_int);
    printf("%d\n", *(int *)el_max);

    el_max = mymax(arr_char, 20, sizeof(char), &gt_char);
    printf("%c\n", *(char *)el_max);

    el_max = mymax(arr_str, 11, sizeof(char *), &gt_str);
    printf("%s\n", *(char **)el_max);
}