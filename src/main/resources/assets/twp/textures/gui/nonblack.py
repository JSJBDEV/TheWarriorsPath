from PIL import Image
img = Image.open('star_01.png')
img = img.convert("RGBA") # create the pixel map

for i in range(img.height): # for every pixel:
    for j in range(img.width):
        #print(img.getpixel((i,j)))
        if img.getpixel((i,j)) == (0,0,0,255):
            # change to black if not red
            
            img.putpixel((i,j),(0,0,0,0))
img.save('c_star_01.png')


for star in range(9):
    img = Image.open('star_0'+str(star+1)+'.png')
    img = img.convert("RGBA") # create the pixel map

    for i in range(img.height): # for every pixel:
        for j in range(img.width):
            #print(img.getpixel((i,j)))
            if img.getpixel((i,j)) == (0,0,0,255):
                # change to black if not red
                
                img.putpixel((i,j),(0,0,0,0))
    img.save('star_0'+str(star+1)+'.png')