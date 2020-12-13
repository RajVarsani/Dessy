# COD-Project-TB404


## Aim of project
Making easier to make illustration by providing expected visulitasion of image ( for using as refernce while making illustrations ) that how it will look like after making illustration which affects productivity of a designer.

## Base Functioning :
Change value of pixals of an image contained in respective segment of values to same value so that it looks like it has been illustrated.

### base logic : 
take rgb value of an pixal , devide by a number ( strengh of the filter which is taken as input from user ) (as value of any of r , g and b are integer so when we devide that by an integer the value of result will be base line (Ex. 255/16 =15), 241/16=15) and when we multiply that by same number it will create segments of length as same as the value which was taken as stengh of filter ( Ex : if value of strengh of filter is 16 and if value of any of r >240 resultant will be 240, if the value will be between 224 and 240 then resultant will be 224, similarly it creates a diffence of 16 in every color and make segments of 16 so that in the resultat image there will we many same segments of pixal partition which have same value of colours. ) and that will result into a image which has similar look as of an illustration and that will help reducing illustration making process time drastically as a huge portion of time in illusration making process is occupied by visulising finale result and which colours to make result as optimum as possible.

We have also added options for adding extra rgb values to every pixal because when we apply this filter it is making this possible by reducing the value of color, so color gets dimmer when value of StrenghOfFilter is high such as 32, 64, 12
