/**
 * 防止Render免费tier休眠的工具
 *
 * Render免费tier 15分钟无请求会休眠
 * 使用此工具定期发送请求保持服务活跃
 */

// 后端地址
const BACKEND_URL = 'https://live-fullstack-backend.onrender.com';
const GATEWAY_URL = 'https://live-fullstack-gateway.onrender.com';

// 心跳间隔（5分钟）
const HEARTBEAT_INTERVAL = 5 * 60 * 1000;

// 心跳定时器
let heartbeatTimer = null;

/**
 * 发送心跳请求
 */
async function sendHeartbeat() {
    try {
        // 请求后端健康检查接口
        const response = await fetch(`${BACKEND_URL}/api/admin/live/status`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (response.ok) {
            console.log('✅ 心跳成功，服务保持活跃');
        } else {
            console.warn('⚠️ 心跳响应异常:', response.status);
        }
    } catch (error) {
        console.warn('⚠️ 心跳请求失败:', error.message);
    }
}

/**
 * 启动心跳
 */
export function startHeartbeat() {
    if (heartbeatTimer) {
        console.log('⚠️ 心跳已在运行');
        return;
    }

    console.log('🚀 启动心跳，防止服务休眠');
    console.log(`📡 后端地址: ${BACKEND_URL}`);
    console.log(`⏱️ 心跳间隔: ${HEARTBEAT_INTERVAL / 1000}秒`);

    // 立即发送一次心跳
    sendHeartbeat();

    // 设置定时器
    heartbeatTimer = setInterval(sendHeartbeat, HEARTBEAT_INTERVAL);
}

/**
 * 停止心跳
 */
export function stopHeartbeat() {
    if (heartbeatTimer) {
        clearInterval(heartbeatTimer);
        heartbeatTimer = null;
        console.log('🛑 心跳已停止');
    }
}

/**
 * 检查服务状态
 */
export async function checkServiceStatus() {
    try {
        const response = await fetch(`${BACKEND_URL}/api/admin/live/status`);
        const data = await response.json();

        return {
            backend: true,
            data: data
        };
    } catch (error) {
        return {
            backend: false,
            error: error.message
        };
    }
}

// 默认导出
export default {
    startHeartbeat,
    stopHeartbeat,
    checkServiceStatus
};
