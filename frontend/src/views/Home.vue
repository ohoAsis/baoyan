<template>
  <div class="home">
    <h1>欢迎使用保研助手系统</h1>
    <p>前端框架已成功运行！</p>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.home h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.home p {
  font-size: 1.5rem;
}
</style>

