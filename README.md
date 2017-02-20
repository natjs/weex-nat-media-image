# nat-media-image

## Installation
```
weexpack plugin add nat-media-image
```

```
npm install weex-nat --save
```

## Usage

Use in weex file (.we)

```html
<script>
import 'Nat' from 'weex-nat'

// preview
Nat.image.preview([
    'http://domain.com/image/1',
    'http://domain.com/image/2',
    'http://domain.com/image/3',
])

// pick image from gallery
Nat.image.pick((err, ret) => {
    console.log(ret)
})

// get info
Nat.image.info('http://domain.com/image', (err, ret) => {
    console.log(ret)
})

// get exif info
Nat.image.exif('http://domain.com/image', (err, ret) => {
    console.log(ret)
})

</script>
```

See the Nat [Documentation](http://natjs.com/) for more details.
