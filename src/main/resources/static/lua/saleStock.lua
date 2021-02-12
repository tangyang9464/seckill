--判断商品是否存在(-1)
if( redis.call('exists',KEYS[1])==1 )
then
    --判断是否当前用户是否购买过限购(-2)
    if( redis.call('exists',KEYS[2])==0 )
    then
        local value = redis.call('GET',KEYS[1]);
        local stock = tonumber(value);
        if( stock > 0 )
        then
            return redis.call('DECR',KEYS[1]);
        else
            return -1;
        end
    else
        --表示限购
        return -2;
    end
else
    return -1;
end