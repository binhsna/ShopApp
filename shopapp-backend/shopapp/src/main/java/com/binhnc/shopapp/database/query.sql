select p.id,c.id,c.name,p.created_at,p.description,p.name,p.price,p.thumbnail,p.updated_at
from products p left join categories c on c.id=p.category_id where p.id=1;