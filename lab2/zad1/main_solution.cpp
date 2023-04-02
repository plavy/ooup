#include <iostream>
#include <stdlib.h>
#include <list>

struct Point
{
    int x;
    int y;
};

class Shape
{
public:
    virtual void draw() = 0;
    virtual void move(int, int ) = 0;
};
class Square : Shape
{
private:
    double side_;
    Point center_;

public:
    virtual void draw()
    {
        std::cout << "Center: " << this->center_.x << ", " << this->center_.y << "\n";
    }
    virtual void move(int x, int y)
    {
        this->center_.x += x;
        this->center_.y += y;
    }
};
class Circle : Shape
{
private:
    double radius_;
    Point center_;

public:
    virtual void draw()
    {
        std::cout << "Center: " << this->center_.x << ", " << this->center_.y << "\n";
    }
    virtual void move(int x, int y)
    {
        this->center_.x += x;
        this->center_.y += y;
    }
};
class Rhomb : Shape
{
private:
    double e_;
    double f_;
    Point center_;

public:
    virtual void draw()
    {
        std::cout << "Center: " << this->center_.x << ", " << this->center_.y << "\n";
    }
    virtual void move(int x, int y)
    {
        this->center_.x += x;
        this->center_.y += y;
    }
};

void drawShapes(const std::list<Shape *> &fig)
{
    std::list<Shape *>::const_iterator it;
    for (it = fig.begin(); it != fig.end(); ++it)
    {
        (*it)->draw();
    }
}

void moveShapes(const std::list<Shape *> &fig, int x, int y)
{
    std::list<Shape *>::const_iterator it;
    for (it = fig.begin(); it != fig.end(); ++it)
    {
        (*it)->move(x, y);
    }
}

int main()
{
    std::list<Shape *> shapes;
    shapes.push_back((Shape *)new Circle);
    shapes.push_back((Shape *)new Square);
    shapes.push_back((Shape *)new Square);
    shapes.push_back((Shape *)new Circle);
    shapes.push_back((Shape *)new Rhomb);

    drawShapes(shapes);
    moveShapes(shapes, 2, 1);
    drawShapes(shapes);
}